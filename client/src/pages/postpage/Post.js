import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
const Post = ({ post }) => {
  const navigate = useNavigate();
  const [isbookmark, setIsBookmark] = useState(false);
  const [mybookmark, setMyBookmark] = useState([]);

  useEffect(() => {
    axios(`https://seb-006.shop/api/members/my-bookmark`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
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
    axios(`https://seb-006.shop/api/members/bookmark?postId=${post.postId}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    });
  };

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
          <span> 게시글 아이디 : {post.postId} </span>
          <button
            onClick={() => {
              bookmarkHandler();
            }}
          >
            {isbookmark ? "❤️" : "🤍"}
          </button>
        </span>
      </div>
    </div>
  );
};

export default Post;
