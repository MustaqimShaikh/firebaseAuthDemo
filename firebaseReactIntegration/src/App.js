import './App.css';

import { GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import { createUserWithEmailAndPassword, signInWithEmailAndPassword } from "firebase/auth";

import auth from './firebase_setup/firebase';

function App() {
  
  const googleUserLogin = () => {
    const provider = new GoogleAuthProvider();
    signInWithPopup(auth, provider)
    .then((result) => {
      auth.currentUser.getIdToken(true).then(function(idToken) {
        console.log("Main Token: ", idToken);
  
      }).catch(function(error) {
      });
        const credential = GoogleAuthProvider.credentialFromResult(result);
        const token = credential.accessToken;
        console.log("token:: ", result);
        const user = result.user;
        console.log("user:: ", credential);
        // ...
    }).catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        const email = error.customData.email;
        const credential = GoogleAuthProvider.credentialFromError(error);
        console.log(errorMessage);
    });
  }
  const userSignIn = () => {
    const email = document.getElementById('email').value;
    const pwd = document.getElementById('pwd').value;
    createUserWithEmailAndPassword(auth, email, pwd)
    .then((userCredential) => {
      const user = userCredential.user;
      console.log(user);
    })
    .catch((error) => {
      const errorCode = error.code;
      const errorMessage = error.message;
      console.log(errorMessage);
    });
  }
  const userLogin = () => {
    const email = document.getElementById('email').value;
    const pwd = document.getElementById('pwd').value;
    signInWithEmailAndPassword(auth, email, pwd)
    .then((userCredential) => {
    auth.currentUser.getIdToken().then(function(idToken) {
      console.log("Mani Token: ", idToken);

    }).catch(function(error) {
      // Handle error
    });
    const user = userCredential.user;
    console.log(user);
  })
  .catch((error) => {
    const errorCode = error.code;
    const errorMessage = error.message;
    console.log("Invalid user");
  });
  }
  return (
    <div className="App">
      <div className='input-item'>
        <label>Email</label>
        <input type='email' id='email'/>
      </div>
      <div className='input-item'>
        <label>Password</label>
        <input type='password' id='pwd'/>
      </div>
      <div className='button-item'>
        <button onClick={userSignIn}>Sign Up</button> 
        <button onClick={userLogin}>Sign In</button> 
        <button onClick={googleUserLogin}>Sign Up With Google</button> 
      </div>
    </div>
  );
}

export default App;
