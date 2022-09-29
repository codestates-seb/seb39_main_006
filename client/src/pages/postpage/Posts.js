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
    <Container>
      {data.map((post) => (
        <div key={post.postId}>
          <Post post={post} />
        </div>
      ))}
    </Container>
  );
};

export default Posts;

const Container = styled.div`
  border: 1px solid black;
  flex-grow: 1;
`;
