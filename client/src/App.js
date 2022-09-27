import { Fragment } from "react";
import Header from "./components/Header";
import MainPage from "./components/MainPage";
import Auth from "./components/Auth";
import PostDetail from "./components/PostDetail";
import EditPost from "./components/EditPost";
import NewPost from "./components/NewPost";
// import UserProfile from "./components/UserProfile";
import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <Fragment>
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
    </Fragment>
  );
}

export default App;
