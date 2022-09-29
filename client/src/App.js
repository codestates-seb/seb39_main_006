import React from "react";
import Header from "./components/Header";
import MainPage from "./pages/mainpage/MainPage";
import Auth from "./components/Auth";
import PostDetail from "./pages/postpage/PostDetail";
import EditPost from "./pages/postpage/EditPost";
import NewPost from "./pages/postpage/NewPost";
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
