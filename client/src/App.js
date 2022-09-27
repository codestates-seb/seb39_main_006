import React from "react";
import Header from "./components/Header";
import MainPage from "./Pages/MainPage/MainPage";
import Auth from "./components/Auth";
import PostDetail from "./Pages/PostPage/PostDetail";
import EditPost from "./Pages/PostPage/EditPost";
import NewPost from "./Pages/PostPage/NewPost";
import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        {!sessionStorage.getItem("isLogin") ? (
          <Route path="/" element={<Auth />}></Route>
        ) : (
          <>
            <Route path="/auth" element={<MainPage />}></Route>
            <Route path="/:id" element={<PostDetail />}></Route>
            <Route path="/edit/:id" element={<EditPost />}></Route>
            <Route path="/new" element={<NewPost />}></Route>
          </>
        )}
      </Routes>
    </div>
  );
}

export default App;
