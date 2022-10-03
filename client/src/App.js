import React from "react";
import Header from "./components/Header";
import MainPage from "./pages/mainpage/MainPage";
import PostDetail from "./pages/postpage/PostDetail";
import EditPost from "./pages/postpage/EditPost";
import NewPost from "./pages/postpage/NewPost";
import { Routes, Route } from "react-router-dom";
import Login from "./pages/account/Login";
import SignUp from "./pages/account/SignUp";
import Matching from "./pages/postpage/Matching";
import UserInfo from "../src/pages/mypage/UserInfo";
import Background from "../src/components/ui/Background";
import MyPage from "./pages/mypage/MyPage";
import MyPost from "./pages/mypage/MyPost";
import MyBookmark from "./pages/mypage/MyBookmark";
import MyMatching from "./pages/mypage/MyMatching";
function App() {
  return (
    <Background>
      <Header />

      <Routes>
        {sessionStorage.getItem("isLogin") === null ? (
          <>
            <Route path="/signup" element={<SignUp />}></Route>
            <Route path="/" element={<Login />}></Route>
          </>
        ) : (
          <>
            <Route path="/main" element={<MainPage />} />
            <Route path="/new" element={<NewPost />} />
            <Route path="/:id" element={<PostDetail />} />
            <Route path="/edit/:id" element={<EditPost />} />
            <Route path="/match/:matchid" element={<Matching />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/userinfo" element={<UserInfo />} />
            <Route path="/mybookmark" element={<MyBookmark />} />
            <Route path="/mypost" element={<MyPost />} />
            <Route path="/mymatchinfo" element={<MyMatching />} />
          </>
        )}
      </Routes>
    </Background>
  );
}

export default App;
