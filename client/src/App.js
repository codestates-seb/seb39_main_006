import { Fragment } from "react";
import { useSelector } from "react-redux";
import Header from "./components/Header";
import MainPage from "./components/MainPage";
import Auth from "./components/Auth";
import PostDetail from "./components/PostDetail";
import EditPost from "./components/EditPost";
import NewPost from "./components/NewPost";
// import UserProfile from "./components/UserProfile";
import classes from "./App.module.css";
import { Routes, Route } from "react-router-dom";

function App() {
  const isAuth = useSelector((state) => state.auth.isAuthenticated);
  return (
    <Fragment>
      <Header />
      <Routes>
        {console.log(isAuth)}
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
