import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import axios from "axios";
import styled from "styled-components";
import CheckDisplayName from "../account/CheckDisplayName";

const Userinfo = () => {
  const [isDisabledInfo, setIsDisabledInfo] = useState(false);
  const [imageURL, setImageURL] = useState("");
  const [userInfo, setUserInfo] = useState("");

  const displaynameInputRef = useRef();
  const passwordInputRef = useRef();
  const phoneInputRef = useRef();
  const contentInputRef = useRef();
  const profileuploadInputRef = useRef();

  // displaynameInputRef.current.value = accountInfo.displayName;
  //#region Show validate text
  const [validateDisplayNameText, setValidateDisplayNameText] = useState("");
  const [validatePasswordText, setValidatePasswordText] = useState("");
  const [validatePhoneNumberText, setValidatePhoneNumberText] = useState("");
  //#endregion

  //#region Change validate className
  const [
    validateDisplayNameNoticeClassname,
    setValidateDisplayNameNoticeClassname,
  ] = useState("validate");
  const [validatePasswordNoticeClassname, setValidatePasswordNoticeClassname] =
    useState("validate");
  const [
    validatePhoneNumberNoticeClassname,
    setValidatePhoneNumberNoticeClassname,
  ] = useState("validate");
  //#endregion

  //#region Check validate&Duplicate functions
  const validatePhoneNumber = (phoneNumber) => {
    return String(phoneNumber).match(/^\d{3}-\d{3,4}-\d{4}$/);
  };

  const onClickDuplicateDisplayName = async () => {
    const enteredDisplayName = displaynameInputRef.current.value;
    if (enteredDisplayName.length > 1) {
      const isDuplicateDisplayName = await CheckDisplayName(
        displaynameInputRef.current.value
      );

      setIsDisabledInfo(isDuplicateDisplayName);

      if (isDuplicateDisplayName) {
      }
    } else {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    }
  };
  //#endregion

  //#region OnChanged event for validate
  const onChangedDisplayName = (e) => {
    if (e.target.value.length < 2) {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    } else {
      setValidateDisplayNameText("✅ 올바른 닉네임 형식입니다.");
      setValidateDisplayNameNoticeClassname("HTH-green");
    }
  };

  const onChangedPassword = (e) => {
    const enteredPassword = e.target.value;
    if (enteredPassword.length < 6) {
      setValidatePasswordText("❌ 비밀번호 6글자 이상 입력해주세요");
      setValidatePasswordNoticeClassname("validate");
    } else {
      setValidatePasswordText("✅ 올바른 비밀번호 형식입니다.");
      setValidatePasswordNoticeClassname("HTH-green");
    }
  };

  const onChangedPhoneNumber = (e) => {
    if (validatePhoneNumber(e.target.value) === null) {
      setValidatePhoneNumberText("❌ 000-0000-0000 형식으로 입력해 주세요");
      setValidatePhoneNumberNoticeClassname("validate");
    } else {
      setValidatePhoneNumberText("✅ 올바른 번호 형식입니다.");
      setValidatePhoneNumberNoticeClassname("HTH-green");
    }
  };

  const onChangedContent = (e) => {
    // setContentText(e.target.value);
  };
  //#endregion

  // if (accountInfo.phone !== null) {
  //   // setPhoneNumberText(accountInfo.phone);
  // }
  // if (accountInfo.content !== null) {
  //   // setContentText(accountInfo.content);
  // }

  const postImg = (e) => {
    const formData = new FormData();
    formData.append("image", e.target.files[0]);
    axios(`https://seb-006.shop/api/images/upload`, {
      method: "POST",
      headers: {
        "Content-Type": "multipart/form-data",
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
      data: formData,
    }).then((res) => {
      // 기홍님의 잔재.....
      // let testid = res.data.imageId;
      // setImageId(testid);
      // mount.current = true;
      setImageURL(res.data.imageUrl);
    });
  };

  const usesubmitHandler = (event) => {
    event.preventDefault();

    const enteredDisplayName = displaynameInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredPhone = phoneInputRef.current.value;
    const enteredContent = contentInputRef.current.value;

    if (
      validatePasswordNoticeClassname === "validate" ||
      validatePhoneNumberNoticeClassname === "validate"
    ) {
      alert("서식을 확인해 주세요");
      return;
    }

    axios(`${process.env.REACT_APP_URL}/api/members?`, {
      method: "PATCH",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
      data: {
        displayName: enteredDisplayName,
        password: enteredPassword,
        phone: enteredPhone,
        content: enteredContent,
        //TODO useState 로 이미지 url을 받아온다
        profileImage: imageURL,
      },
    })
      .then((res) => {
        if (res.status) {
          alert("정상적으로 수정되었습니다. 로그인하여 진행해주세요.");
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

  const memberId = sessionStorage.getItem("memberId");

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_URL}/api/members/${memberId}`)
      .then((res) => {
        setValidatePhoneNumberText("✅ 올바른 번호 형식입니다.");
        setValidatePhoneNumberNoticeClassname("HTH-green");
        setUserInfo(res.data);
      });
  }, []);

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
                    onChange={onChangedDisplayName}
                    defaultValue={userInfo !== null ? userInfo.displayName : ""}
                  />
                </div>
                <Button onClick={onClickDuplicateDisplayName}>중복확인</Button>
                <p className={validateDisplayNameNoticeClassname}>
                  {validateDisplayNameText}
                </p>

                <label htmlFor="password">Password</label>
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
                    onChange={onChangedPassword}
                  />
                </div>
                <p className={validatePasswordNoticeClassname}>
                  {validatePasswordText}
                </p>

                <label htmlFor="phone">Phone Number</label>
                <div className="container">
                  <input
                    disabled={isDisabledInfo}
                    className="input-tag"
                    type="phone"
                    id="phone"
                    required
                    ref={phoneInputRef}
                    defaultValue={userInfo !== null ? userInfo.phone : ""}
                    onChange={onChangedPhoneNumber}
                    // value={phoneNumberText}
                  />
                </div>
                <p className={validatePhoneNumberNoticeClassname}>
                  {validatePhoneNumberText}
                </p>

                <label htmlFor="content">자기소개</label>
                <div className="container">
                  <input
                    disabled={isDisabledInfo}
                    className="input-tag"
                    type="textarea"
                    id="content"
                    required
                    ref={contentInputRef}
                    defaultValue={userInfo !== null ? userInfo.content : ""}
                    onChange={onChangedContent}
                  />
                </div>

                <label htmlFor="content">프로필 사진 업로드</label>
                <div className="container">
                  <input
                    className="input-tag"
                    type="file"
                    id="profile-upload"
                    accept="image/*"
                    required
                    onChange={postImg}
                    ref={profileuploadInputRef}
                  />
                </div>
              </form>
            </InputWrapper>
            <div>
              <Button onClick={usesubmitHandler}>수정하기</Button>
              <Button onClick={() => navigate("/mypage")}>취소</Button>
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
