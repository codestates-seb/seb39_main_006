import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
const Post = ({ post }) => {
  const navigate = useNavigate();
  const [isbookmark, setIsBookmark] = useState(false);
  const [mybookmark, setMyBookmark] = useState([]);

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/members/my-bookmark`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      setMyBookmark(res.data.postIds);
    });
  }, []);

  useEffect(() => {
    mybookmark.map((el) => (el === post.postId ? setIsBookmark(true) : null));
  }, [mybookmark, post.postId]);

  const bookmarkHandler = () => {
    setIsBookmark(!isbookmark);
    axios(
      `${process.env.REACT_APP_URL}/api/members/bookmark?postId=${post.postId}`,
      {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      }
    );
  };

  return (
    <div>
      <PostTitle>
        <span
          onClick={() => {
            navigate(`/${post.postId}`);
          }}
        >
          {post.title}
        </span>
        <span>
          <span className="participants">
            <span>모집 인원 </span>
            <span>
              {post.participantsCount} / {post.totalCount}
            </span>
          </span>
          <button
            onClick={() => {
              bookmarkHandler();
            }}
          >
            {isbookmark ? "❤️" : "🤍"}
          </button>
        </span>
      </PostTitle>
    </div>
  );
};

export default Post;

const PostTitle = styled.div`
  display: flex;
  justify-content: space-between;
  button {
    margin-left: 15px;
  }
  .participants {
  }
`;
