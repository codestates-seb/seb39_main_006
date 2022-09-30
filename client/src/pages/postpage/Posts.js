import axios from "axios";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Post from "./Post";

const Posts = () => {
  const [data, setData] = useState([]);
  useEffect(() => {
    axios(`https://seb-006.shop/api/posts?page=1&size=10`).then((res) => {
      setData([...res.data.data]);
    });
  }, []);
  return (
    <StyledPost>
      <div className="wrapper">
        <div className="contents">
          {data.map((post) => (
            <div className="post" key={post.postId}>
              <Post post={post} />
            </div>
          ))}
        </div>
      </div>
    </StyledPost>
  );
};

export default Posts;
const StyledPost = styled.div`
  display: inline-block;
  .wrapper {
    width: 750px;
    height: 700px;
    background-color: #d5eaf1;
    border-radius: 10px;
    position: absolute;
    box-shadow: 0px 3px 10px 1px rgba(0, 0, 0, 0.3);
  }
  .contents {
    padding: 3.5em;
    position: relative;
    height: 65%;
  }
  .post {
    margin: 1rem;
    padding: 10px;
    border-radius: 2px;
    outline: none;
    width: 38rem;
    display: inline-block;
    border: none;
    border-radius: 5px;
    border: 1.5px solid #a19f9f;
    background-color: white;
    font-size: large;
  }
`;
