import React from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const Post = ({ post }) => {
  const navigate = useNavigate();

  return (
    <div>
      <Test>
        <span
          onClick={() => {
            navigate(`/${post.postId}`);
          }}
        >
          {post.title}
        </span>
        <span>
          <span> 모집 인원</span>
          <span>
            {post.participantsCount} / {post.totalCount}
          </span>
        </span>
      </Test>
    </div>
  );
};

export default Post;

const Test = styled.div`
  border: 1px solid black;
`;
