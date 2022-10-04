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
import MyPage from "./pages/mypage/MyPage";
import MyPost from "./pages/mypage/MyPost";
import MyBookmark from "./pages/mypage/MyBookmark";
import MyMatching from "./pages/mypage/MyMatching";
import styled from "styled-components";
import imgBgr from "../src/img/background.png";
function App() {
  return (
    <Wrap>
      <img
        id="bgr"
        src={imgBgr}
        alt="./background.png"
        width="2000"
        height="2800"
      />
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
    </Wrap>
  );
}

export default App;
const Wrap = styled.div`
  #bgr {
    position: absolute;
    top: -600px;
    background-repeat: repeat;
    z-index: -995;
    opacity: 50%;
    min-width: 1000px;
    max-height: fit-content;
    /* @media screen and (max-width: 1500px) {
      padding: 30px 25px 30px 25px;
      height: 455px;
    } */
  }
`;
