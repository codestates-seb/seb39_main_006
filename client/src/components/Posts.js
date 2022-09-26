import axios from "axios";
import React, { useEffect, useState } from "react";
import classes from "./Posts.module.css";
import Post from "./Post";

const Posts = () => {
  const [data, setData] = useState([]);
  useEffect(() => {
    axios(`https://seb-006.shop/api/posts?page=1&size=10`).then((res) => {
      setData([...res.data.data]);
    });
  }, []);
  return (
    <div className={classes.container}>
      {data.map((post) => (
        <div key={post.postId}>
          <Post post={post} />
        </div>
      ))}
    </div>
  );
};

export default Posts;
