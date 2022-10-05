import SideBar from "../../components/SideBar";
import styled from "styled-components";
import Button from "../../components/ui/Button";
import WrapperBox from "../../components/ui/WrapperBox";
import H1 from "../../components/ui/H1";
import axios from "axios";
import { useEffect, useState } from "react";
const MyPage = () => {
  const [userInfo, setUserInfo] = useState("");
  const memberId = sessionStorage.getItem("memberId");

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_URL}/api/members/${memberId}`, {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      })
      .then((res) => {
        setUserInfo(res.data);
      });
  }, []);

  return (
    <>
      <SideBar />
      <Section>
        <section>
          <H1>My Page</H1>
          <h2>여행동행자 모집합니다</h2>
          <section>
            <a href="/userinfo">
              <Button>유저정보 수정하기</Button>
            </a>
          </section>
          <UserDiv>
            <Container>
              <div className="label-wrapper">
                <label htmlFor="displayName">User Name</label>
                <div className="container">
                  <input
                    readOnly
                    className="input-tag"
                    defaultValue={userInfo !== null ? userInfo.displayName : ""}
                  ></input>
                </div>

                <label htmlFor="phone">Phone Number</label>
                <div className="container">
                  <input
                    readOnly
                    className="input-tag"
                    defaultValue={userInfo !== null ? userInfo.phone : ""}
                  ></input>
                </div>

                <label htmlFor="content">자기소개</label>
                <div className="container">
                  <input
                    readOnly
                    className="input-tag"
                    type="textarea"
                    defaultValue={userInfo !== null ? userInfo.content : ""}
                  ></input>
                </div>

                <label htmlFor="profile-img">프로필 사진</label>
                <div className="container">
                  <img
                    src={userInfo !== null ? userInfo.profileImage : ""}
                    width="100"
                    height="100"
                  ></img>
                </div>
              </div>
            </Container>
          </UserDiv>
          <WrapperBox></WrapperBox>
        </section>
      </Section>
    </>
  );
};
export default MyPage;
const Container = styled.div`

  label {
    font-size: 1.5rem;
    font-weight: 500;
    color: #547882;
  }

  margin: 150px 0 250px 0;
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  min-height: 30rem;
  max-width: 468px;
  width: 100%;
  height: fit-content;
  background: #fbfbfb;
  box-shadow: 0px 0px 11px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-family: Roboto;
  box-sizing: border-box;
  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 455px;
  }
`;
const UserDiv = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 5px 0 5px;
  margin-left: -70vw;
  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 700px;
  }
`;
const Section = styled.div`
  section {
    margin-top: 2rem;
    display: flex;
    position: relative;
    left: 110px;
    list-style: none;
    section {
      display: inline-block;
      margin: 3rem;
    }
  }

  h1 {
    font-size: 52px;
    display: block;
    align-items: center;
    font-family: "Montserrat", sans-serif;
    text-transform: uppercase;
    text-shadow: 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9, 3px 3px 0px #dabbc9,
      4px 4px 0px #dabbc9;
  }
  h2 {
    padding: 1rem;
    font-size: 15px;
    display: block;
    align-items: center;
    font-family: "Montserrat", sans-serif;
    text-transform: uppercase;
    text-shadow: 1px 1px 0px #dabbc9, 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9,
      3px 3px 0px #dabbc9;
  }

  textarea:read-only,
  input:read-only {
    border: 0;
    box-shadow: none;
    background-color: white;
    appearance: none;
    outline: none;
    pointer-events: none;
    &:focus {
      border: 0;
      box-shadow: none;
      background-color: white;
      appearance: none;
    }
    &:focus-visible {
      border: 0;
      box-shadow: none;
      background-color: white;
      appearance: none;
    }
  }
`;
