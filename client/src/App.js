import React from "react";
import Header from "./components/Header";
import MainPage from "./pages/mainpage/MainPage";
import PostDetail from "./pages/postpage/PostDetail";
import EditPost from "./pages/postpage/EditPost";
import NewPost from "./pages/postpage/NewPost";
import Test from "./pages/mypage/Matching";
import { Routes, Route } from "react-router-dom";
import Login from "./pages/account/Login";
import SignUp from "./pages/account/SignUp";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        {sessionStorage.getItem("isLogin") === null ? (
          <>
            <Route path="/signup" element={<SignUp />}></Route>
            <Route path="/" element={<Login />}></Route>
          </>
        ) : (
          <>
            <Route path="/main" element={<MainPage />}></Route>
            <Route path="/:id" element={<PostDetail />}></Route>
            <Route path="/edit/:id" element={<EditPost />}></Route>
            <Route path="/new" element={<NewPost />}></Route>
            <Route path="/match/:matchid" element={<Test />}></Route>
          </>
        )}
      </Routes>
    </div>
  );
}

export default App;
