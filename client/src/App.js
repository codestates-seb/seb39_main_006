import { Fragment } from "react";
import { useSelector } from "react-redux";
import Header from "./components/Header";
import MainPage from "./components/MainPage";
import Auth from "./components/Auth";
import PostDetail from "./components/PostDetail";
// import UserProfile from "./components/UserProfile";
import classes from "./App.module.css";
import { Routes, Route } from "react-router-dom";

function App() {
  const isAuth = useSelector((state) => state.auth.isAuthenticated);
  return (
    <Fragment>
      <Header />
      <Routes>
        {!isAuth && <Route path="/auth" element={<Auth />}></Route>}
        {isAuth && (
          <>
            <Route path="/" element={<MainPage />}></Route>
            <Route path="/:id" element={<PostDetail />}></Route>
          </>
        )}
      </Routes>
    </Fragment>
  );
}

export default App;
