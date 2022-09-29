import React from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const MainSubHeader = () => {
  const navigate = useNavigate();
  return (
    <SubHeader>
      <input></input>
      <button>검색</button>
      <button
        onClick={() => {
          navigate(`/new`);
        }}
      >
        게시글 작성
      </button>
    </SubHeader>
  );
};

export default MainSubHeader;

const SubHeader = styled.div`
  border: 1px solid black;
`;
