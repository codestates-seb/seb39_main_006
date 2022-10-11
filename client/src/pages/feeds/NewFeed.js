import axios from "axios";
import React, { useRef } from "react";
import { useNavigate } from "react-router-dom";

const NewFeed = () => {
  const navigate = useNavigate();
  const feedBody = useRef();

  const onSubmitFeed = () => {
    axios(`${process.env.REACT_APP_URL}/api/feeds`, {
      method: "POST",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
      data: { body: feedBody.current.value, image: [] },
    }).then(() => {
      navigate(`/feeds`);
    });
  };
  return (
    <div>
      <textarea placeholder="작성해주세요~" ref={feedBody} />
      <button
        onClick={() => {
          onSubmitFeed();
        }}
      >
        피드작성
      </button>
      <button
        onClick={() => {
          navigate(`/feeds`);
        }}
      >
        취소
      </button>
    </div>
  );
};

export default NewFeed;
