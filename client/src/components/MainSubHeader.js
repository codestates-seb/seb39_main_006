import React from "react";
import { useNavigate } from "react-router-dom";
import classes from "./MainSubHeader.module.css";

const MainSubHeader = () => {
  const navigate = useNavigate();
  return (
    <div className={classes.container}>
      <input></input>
      <button>검색</button>
      <button
        onClick={() => {
          navigate(`/new`);
        }}
      >
        게시글 작성
      </button>
    </div>
  );
};

export default MainSubHeader;
