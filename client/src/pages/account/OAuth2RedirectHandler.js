import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import queryString from "query-string";
import jwt_decode from "jwt-decode";

const OAuth2RedirectHandler = () => {
  const { search } = useLocation();
  const queryObj = queryString.parse(search);
  const { token } = queryObj;
  const decode = jwt_decode(token);
  const { id, displayName } = decode;

  sessionStorage.setItem("AccessToken", token);
  sessionStorage.setItem("isLogin", true);
  sessionStorage.setItem("memberId", id);
  sessionStorage.setItem("userName", displayName);
  window.location.replace("/main");
  return <Navigate to="/main" replace={true}></Navigate>;
};

export default OAuth2RedirectHandler;
