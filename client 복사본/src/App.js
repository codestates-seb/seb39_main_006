import React from "react";
import Header from "./components/Header";
import Main from "./pages/mainpage/Main";
import PostDetail from "./pages/postpage/PostDetail";
import EditPost from "./pages/postpage/EditPost";
import NewPost from "./pages/postpage/NewPost";
import { Routes, Route } from "react-router-dom";
import Login from "./pages/account/Login";
import SignUp from "./pages/account/SignUp";
import Matching from "./pages/postpage/Matching";
import UserInfo from "./pages/mypage/UserInfo";
import MyPage from "./pages/mypage/MyPage";
import MyPost from "./pages/mypage/MyPost";
import MyBookmark from "./pages/mypage/MyBookmark";
import MyMatching from "./pages/mypage/MyMatching";
import styled from "styled-components";
import imgBgr from "../src/img/background.png";
import Messages from "./pages/mypage/Messages";
import GlobalStyle from "./GlobalStyle";
import OAuth2RedirectHandler from "./pages/account/OAuth2RedirectHandler";
function App() {
  return (
    <>
      <GlobalStyle />
      <Wrap>
        <Header />

        <Routes>
          {sessionStorage.getItem("isLogin") === null ? (
            <>
              <Route
                path="/oauth2/redirect"
                element={<OAuth2RedirectHandler />}
              />
              <Route path="/signup" element={<SignUp />}></Route>
              <Route path="/" element={<Login />}></Route>
            </>
          ) : (
            <>
              <Route path="/main" element={<Main />} />
              <Route path="/" element={<Login />}></Route>
              <Route path="/new" element={<NewPost />} />
              <Route path="/:id" element={<PostDetail />} />
              <Route path="/edit/:id" element={<EditPost />} />
              <Route path="/match/:matchid" element={<Matching />} />
              <Route path="/mypage" element={<MyPage />} />
              <Route path="/userinfo" element={<UserInfo />} />
              <Route path="/mybookmark" element={<MyBookmark />} />
              <Route path="/mypost" element={<MyPost />} />
              <Route path="/mymatchinfo" element={<MyMatching />} />
              <Route path="/messages" element={<Messages />} />
            </>
          )}
        </Routes>
      </Wrap>
    </>
  );
}

export default App;
const Wrap = styled.div``;
