import React from "react";
import { useNavigate } from "react-router-dom";
const Post = ({ post }) => {
  const navigate = useNavigate();

  return (
    <div>
      <div>
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
      </div>
    </div>
  );
};

export default Post;
