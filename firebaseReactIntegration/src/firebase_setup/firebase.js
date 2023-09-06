// Import the functions you need from the SDKs you need

import { getAnalytics } from "firebase/analytics";
import { getAuth } from "firebase/auth";
import { initializeApp } from "firebase/app";

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyACHtOxG4hvgxdnXX5FQZE2-vLQmxjFJLc",
  authDomain: "apptmartfirebaseauthentication.firebaseapp.com",
  projectId: "apptmartfirebaseauthentication",
  storageBucket: "apptmartfirebaseauthentication.appspot.com",
  messagingSenderId: "255593445048",
  appId: "1:255593445048:web:766d03c15c08d2e1bbe64d",
  measurementId: "G-KK28BMW04T"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const auth = getAuth(app);

export default auth;