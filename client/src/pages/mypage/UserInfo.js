import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import axios from "axios";
import styled from "styled-components";
const Userinfo = () => {
  const [isDisabledInfo, setIsDisabledInfo] = useState(true);

  const onClickDuplicateDisplayName = () => {
    setIsDisabledInfo(setIsDisabledInfo(displaynameInputRef.current.value));

    // const isDisabledValue = setIsDisabledInfo(
    //   displaynameInputRef.current.value
    // );
    // setIsDisabledInfo(isDisabledValue);
  };

  const [validateDisplayNameText, SetValidateDisplayNameText] = useState("");
  const [validatePasswordText, setvalidatePasswordText] = useState("");
  const [validatePhoneNumberText, SetValidatePhoneNumberText] = useState("");
  const [validateContentText, SetValidateContentText] = useState("");

  const displaynameInputRef = useRef();
  const passwordInputRef = useRef();
  const phoneInputRef = useRef();
  const contentInputRef = useRef();

  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );
  };

  const loginHandler = () => {
    navigate("/");
  };

  const usesubmitHandler = (event) => {
    event.preventDefault();

    const enteredDisplayName = displaynameInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredPhone = phoneInputRef.current.value;
    const enteredContent = contentInputRef.current.value;

    if (enteredDisplayName.length < 2) {
      SetValidateDisplayNameText("닉네임을 2글자 이상 입력해 주세요 ");
      return;
    }
    if (enteredPassword.length < 6) {
      setvalidatePasswordText("비밀번호 6글자 이상 입력해주세요 ");
      return;
    }

    if (enteredPhone.length < 13) {
      SetValidatePhoneNumberText("올바른 핸드폰 번호를 입력해 주세요");
      return;
    }
    if (enteredContent === null) {
      SetValidateContentText("소개란을 입력해 주세요");
      return;
    }

    axios(`${process.env.REACT_APP_URL}/api/members?`, {
      method: "PATCH",
      data: {
        displayName: enteredDisplayName,
        password: enteredPassword,
        phone: enteredPhone,
        content: enteredContent,
        profileImage: "",
      },
    })
      .then((res) => {
        if (res.status) {
          alert("정상적으로 회원가입되었습니다. 로그인하여 진행해주세요.");
          navigate("/");
        }
      })
      .catch((err) => {
        if (err.response.status === 401) {
          alert("다시 확인하고 입력해주세요");
          window.location.reload();
        }
      });
  };

  const navigate = useNavigate();
  const submitHandler = (event) => {
    event.preventDefault();
  };
  return (
    <>
      <h1>userinfo Edit</h1>

      <main>
        <section>
          <div>
            <InputWrapper>
              <form onSubmit={(e) => e.preventDefault()}>
                <label htmlFor="displayName">User Name</label>
                <div className="container">
                  <input
                    type="displayName"
                    id="displayName"
                    required
                    ref={displaynameInputRef}
                  />
                </div>
                <Button onClick={onClickDuplicateDisplayName}>중복확인</Button>
                <label htmlFor="password">Password</label>

                <p className="validate">{validateDisplayNameText}</p>
                <div className="container">
                  <input
                    disabled={isDisabledInfo}
                    className="input-tag"
                    type="password"
                    id="password"
                    required
                    ref={passwordInputRef}
                    name="password"
                    autoComplete="off"
                  />
                </div>
                <p className="validate">{validatePasswordText}</p>

                <label htmlFor="phone">Phone Number</label>
                <div className="container">
                  <input
                    disabled={isDisabledInfo}
                    className="input-tag"
                    type="phone"
                    id="phone"
                    required
                    ref={phoneInputRef}
                  />
                </div>
                <p className="validate">{validatePhoneNumberText}</p>

                <label htmlFor="content">자기소개</label>
                <div className="container">
                  <input
                    disabled={isDisabledInfo}
                    className="input-tag"
                    type="textarea"
                    id="content"
                    required
                    ref={contentInputRef}
                  />
                </div>
                <p className="validate">{validateContentText}</p>
              </form>
            </InputWrapper>
            <div>
              <Button onClick={usesubmitHandler}>수정하기</Button>
            </div>
          </div>
        </section>
      </main>
    </>
  );
};

export default Userinfo;

const InputWrapper = styled.div`
  .validate {
    color: red;
    padding: 0.5rem;
  }
  .container {
    display: flex;
    flex-direction: column-reverse;
  }
  .input-tag {
    display: block;
    text-align: left;
    font-size: 1em;
  }
  input {
    padding: 20px 10px;
    border-radius: 5px;
    outline: none;
    width: 30rem;
    display: inline-block;
    border: none;
    border-radius: 5px;
    margin-right: 10%;
    border: 1.5px solid #a19f9f;
  }
  label {
    font-size: large;
    line-height: normal;
    padding: 0.5rem;
  }
  #none + label {
    color: #a19f9f;
  }
  #hover:hover {
    border: 1.5px solid black;
  }
  #hover:hover + label {
    color: black;
  }
  #focus:focus + label {
    color: #2962ff;
  }
  #focus:focus {
    border: 1.5px solid #2962ff;
  }
  .content {
  }
`;
