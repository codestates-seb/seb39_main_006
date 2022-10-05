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
          <PageContainer>
            <ContainerWrap>
              <section className="userinfo">
                <a href="/userinfo">
                  <Button>유저정보 수정하기</Button>
                </a>
              </section>
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
                    id="iamground"
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
            </ContainerWrap>
          </PageContainer>
        </section>
      </Section>
    </>
  );
};
export default MyPage;
const PageContainer = styled.div`
  label {
    font-size: 1.5rem;
    font-weight: 500;
    color: #444;
  }
  position: absolute;

  width: 1000px;
  /*  */
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 5px 0 5px;

  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 700px;
  }
`;
const ContainerWrap = styled.div`
  .label-wrapper {
    font-size: 2rem;
    color: #547882;
    font-weight: 500;
  }
  .input-tag {
    font-size: 1.4rem;
    padding: 1rem;
    width: 20rem;
  }
  #iamground {
    font-size: 1.4rem;
    padding: 1rem;
    width: 50rem;
    height: 10rem;
  }
  .userinfo {
    margin-left: 15rem;
  }
  #author {
    color: darkblue;
    font-weight: 600;
    font-size: 1.3rem;
  }
  .author {
    font-size: 1.25rem;
  }
  button {
    place-items: center;
    font-size: 1.25rem;
    background-color: #dabbc9;
    max-width: 1000px;
    width: fit-content;
    border: 1px solid #dabbc9;
    padding: 0.5rem 1rem;
    margin: 0.5rem;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
    color: #425049;
    &:hover {
      background-color: #efd5c8;
      border-color: #efd5c8;
    }
  }
  .contents {
    padding-left: 1rem;
  }

  margin: 150px 0 250px 0;
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  max-width: 1650px;
  width: 170%;
  height: 900px;
  background-color: beige;

  box-shadow: 0px 0px 11px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-family: Roboto;
  box-sizing: border-box;
  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 455px;
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
