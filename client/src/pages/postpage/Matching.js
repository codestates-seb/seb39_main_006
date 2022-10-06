import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";

const Matching = () => {
  const { matchid } = useParams();
  const [matchData, setMatchData] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/matching/${matchid}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      setMatchData(res.data);
    });
  }, [matchid]);

  const acceptHandler = () => {
    axios(`${process.env.REACT_APP_URL}/api/matching/${matchid}/accept`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    })
      .then(() => {
        navigate(`/${matchData.postId}`);
      })
      .catch((err) => {
        if (err.response.status === 400) {
          if (err.response.data.fieldErrors) {
            alert(err.response.data.fieldErrors[0].reason);
          } else if (
            err.response.data.fieldErrors === null &&
            err.response.data.violationErrors
          ) {
            alert(err.response.data.violationErrors[0].reason);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
        window.location.reload();
      });
  };

  const refuseHandler = () => {
    axios(`${process.env.REACT_APP_URL}/api/matching/${matchid}/refuse`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    })
      .then(() => {
        navigate(`/${matchData.postId}`);
      })
      .catch((err) => {
        if (err.response.status === 400) {
          if (err.response.data.fieldErrors) {
            alert(err.response.data.fieldErrors[0].reason);
          } else if (
            err.response.data.fieldErrors === null &&
            err.response.data.violationErrors
          ) {
            alert(err.response.data.violationErrors[0].reason);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
        window.location.reload();
      });
  };

  return (
    <PageContainer>
      <Container>
        <ContentWrap>
          <p>매칭 게시글 :</p>
          <h1 className="title"> {matchData.postTitle}</h1>
          <p>매칭 신청자 : </p>
          <div className="person">{matchData.memberName}</div>
          <p>매칭 내용 :</p>
          <div className="contents"></div>
          <MatchBody>{matchData.body}</MatchBody>
          <span>
            <Button
              onClick={() => {
                acceptHandler();
              }}
            >
              수락
            </Button>
            <Button
              onClick={() => {
                refuseHandler();
              }}
            >
              거절
            </Button>
          </span>
        </ContentWrap>
      </Container>
    </PageContainer>
  );
};

export default Matching;
const PageContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 5px 0 5px;

  @media screen and (max-width: 1000px) {
    padding: 30px 25px 30px 25px;
    height: 700px;
  }
`;
const Container = styled.div`
  margin: 150px 0 250px 0;
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  max-width: 800px;
  width: 100%;
  height: 900px;
  background-color: #f5f5dc;

  box-shadow: 0px 0px 11px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-family: Roboto;
  box-sizing: border-box;
  @media screen and (max-width: 1000px) {
    padding: 30px 25px 30px 25px;
    height: 455px;
  }
`;
const ContentWrap = styled.div`
  p {
    font-size: 1rem;
  }
  h1 {
    font-size: 2.6rem;
    font-weight: 700;
    color: #666;
  }
  .person {
    font-size: 2rem;
    color: #425049;
    font-weight: 600;
  }
  .contents {
    font-size: 1.5rem;
    color: #425049;
    font-weight: 400;
  }
`;
const MatchBody = styled.div`
  background-color: #d5eaf1;
  width: 70%;
  height: 30vh;
`;

const Button = styled.button`
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
`;
