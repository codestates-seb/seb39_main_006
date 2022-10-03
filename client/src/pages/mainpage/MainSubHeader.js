import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { searchActions } from "../../store/search-slice";

const MainSubHeader = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const title = useSelector((state) => state.search.title);
  const body = useSelector((state) => state.search.body);
  const location = useSelector((state) => state.search.location);
  const startDate = useSelector((state) => state.search.startDate);
  const endDate = useSelector((state) => state.search.endDate);

  return (
    <SubHeader>
      <input
        className="inputtext"
        type="text"
        placeholder="제목을 검색하세요"
        value={title}
        onChange={(e) => {
          dispatch(searchActions.setTitle(e.target.value));
        }}
      ></input>
      <input
        className="inputtext"
        type="text"
        placeholder="본문을 검색하세요"
        value={body}
        onChange={(e) => {
          dispatch(searchActions.setBody(e.target.value));
        }}
      ></input>
      <input
        className="inputtext"
        type="text"
        placeholder="여행지를 검색하세요"
        value={location}
        onChange={(e) => {
          dispatch(searchActions.setLocation(e.target.value));
        }}
      ></input>
      <span>
        <span className="test2">여행 시작 날짜</span>
        <span>
          <input
            type="date"
            value={startDate}
            onChange={(e) => {
              dispatch(searchActions.setStartDate(e.target.value));
            }}
          ></input>
          {/* <div className="test">{startDate}</div> */}
        </span>
      </span>
      <span>
        <span className="test2">여행 종료 날짜</span>
        <input
          type="date"
          value={endDate}
          onChange={(e) => {
            dispatch(searchActions.setEndDate(e.target.value));
          }}
        ></input>
      </span>
      <button>검색</button>
      <button
        onClick={() => {
          navigate(`/new`);
        }}
      >
        게시글 작성
      </button>
      <button
        onClick={() => {
          dispatch(searchActions.setTitle(""));
          dispatch(searchActions.setBody(""));
          dispatch(searchActions.setLocation(""));
          dispatch(searchActions.setStartDate(""));
          dispatch(searchActions.setEndDate(""));
        }}
      >
        초기화
      </button>
    </SubHeader>
  );
};

export default MainSubHeader;

const SubHeader = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-bottom: 30px;
  height: 60px;
  background-color: #d5eaf1;
  border-radius: 10px;
  button {
    font-size: 1rem;
    background-color: #dabbc9;
    width: fit-content;
    border: 1px solid #dabbc9;
    padding: 0.5rem 1rem;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
    color: #425049;
    &:hover {
      background-color: #efd5c8;
      border-color: #efd5c8;
    }
  }
  .inputtext {
    width: 200px;
    height: 40px;
    font-size: 1rem;
    border-radius: 10px;
  }
  .test {
    border: 1px solid black;
  }
`;
