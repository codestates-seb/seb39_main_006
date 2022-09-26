import React from "react";
import classes from "./MainSubHeader.module.css";

const MainSubHeader = () => {
  return (
    <div className={classes.container}>
      <input></input>
      <button>검색</button>
      <button>게시글 작성</button>
    </div>
  );
};

export default MainSubHeader;
