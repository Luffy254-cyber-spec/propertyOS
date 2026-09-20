package com.him.landlordtenant.app.webrtc

import com.google.firebase.database.*
import com.google.gson.Gson
import com.him.landlordtenant.app.data.model.communication.*
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class SignalingClient(
    private val callId: String,
    private val listener: SignalingListener
) {
    private val dbRef = FirebaseDatabase.getInstance().getReference("calls/$callId")
    private val gson = Gson()

    fun sendOffer(sdp: SessionDescription) {
        dbRef.child("offer").setValue(mapOf(
            "sdp" to sdp.description,
            "type" to sdp.type.name
        ))
    }

    fun sendAnswer(sdp: SessionDescription) {
        dbRef.child("answer").setValue(mapOf(
            "sdp" to sdp.description,
            "type" to sdp.type.name
        ))
    }

    fun sendIceCandidate(candidate: IceCandidate, isCaller: Boolean) {
        val candidatePath = if (isCaller) "callerCandidates" else "receiverCandidates"
        dbRef.child(candidatePath).push().setValue(mapOf(
            "sdpMid" to candidate.sdpMid,
            "sdpMLineIndex" to candidate.sdpMLineIndex,
            "sdp" to candidate.sdp
        ))
    }

    fun observeSignaling(isCaller: Boolean) {
        // Observe opposite peer's actions
        if (isCaller) {
            // Caller observes answer and receiver candidates
            dbRef.child("answer").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val sdp = snapshot.child("sdp").getValue(String::class.java) ?: ""
                        val type = snapshot.child("type").getValue(String::class.java) ?: ""
                        listener.onAnswerReceived(SessionDescription(SessionDescription.Type.valueOf(type), sdp))
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            dbRef.child("receiverCandidates").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val candidate = parseCandidate(snapshot)
                    listener.onIceCandidateReceived(candidate)
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            // Receiver observes offer and caller candidates
            dbRef.child("offer").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val sdp = snapshot.child("sdp").getValue(String::class.java) ?: ""
                        val type = snapshot.child("type").getValue(String::class.java) ?: ""
                        listener.onOfferReceived(SessionDescription(SessionDescription.Type.valueOf(type), sdp))
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            dbRef.child("callerCandidates").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val candidate = parseCandidate(snapshot)
                    listener.onIceCandidateReceived(candidate)
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun parseCandidate(snapshot: DataSnapshot): IceCandidate {
        return IceCandidate(
            snapshot.child("sdpMid").getValue(String::class.java) ?: "",
            snapshot.child("sdpMLineIndex").getValue(Int::class.java) ?: 0,
            snapshot.child("sdp").getValue(String::class.java) ?: ""
        )
    }

    interface SignalingListener {
        fun onOfferReceived(description: SessionDescription)
        fun onAnswerReceived(description: SessionDescription)
        fun onIceCandidateReceived(candidate: IceCandidate)
    }
}
