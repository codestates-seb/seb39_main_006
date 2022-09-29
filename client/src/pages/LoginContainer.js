import React from "react";
import { Link } from "react-router-dom";

const LoginContainer = () => {
  return (
    <div>
      <h1>Login</h1>
      <Link to="/login">Log in</Link>
    </div>
  );
};

export default LoginContainer;
