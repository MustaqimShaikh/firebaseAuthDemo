package com.firebase.auth.demo.firebaseAuthDemo.repository;

import com.firebase.auth.demo.firebaseAuthDemo.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserRepository {
	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

	public User save(User user) throws Exception {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> apiFuture = db.collection("user").document(user.getUid()).set(user, SetOptions.merge());
		WriteResult writeResult = apiFuture.get();
		logger.info("Successfully saved, updated time: {}", writeResult.getUpdateTime());
		return user;
	}

	public User getByUid(String uid) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<DocumentSnapshot> apiFuture = db.collection("user").document(uid).get();
		DocumentSnapshot document = apiFuture.get();
		if (document.exists()) {
			logger.info("User found of uid: {}", uid);
			return document.toObject(User.class);
		}
		logger.info("User not found of uid: {}", uid);
		return null;
	}

	public User getByEmail(String email) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection("user").whereEqualTo("email", email).get();
		QueryDocumentSnapshot document = querySnapshotApiFuture.get().getDocuments().get(0);
		if (document.exists()) {
			logger.info("User found of email: {}", email);
			return document.toObject(User.class);
		}
		logger.info("User not found of e: {}", email);
		return null;
	}

}