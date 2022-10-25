import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";

// toast
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";

// toast color syntax
import "tui-color-picker/dist/tui-color-picker.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";

const NewPost = () => {
  const navigate = useNavigate();

  const bodyRef = useRef();
  const [title, setTitle] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [location, setLocation] = useState("");
  const [totalCount, setTotalCount] = useState(0);
  const [closeDate, setCloseDate] = useState("");

  // 기홍님의 잔재....
  // const [imageId, setImageId] = useState(0);
  // const [imageIds, setImageIds] = useState([]);
  // var mount = useRef(false);
  // useEffect(() => {
  //   if (mount.current) {
  //     addItemsToArray(imageId);
  //     mount.current = false;
  //   }
  // }, [addItemsToArray]);

  // function addItemsToArray(imageId) {
  //   setImageIds((imageIds) => [...imageIds, imageId]);
  // }
  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const date = today.getDate();

  const submitHandler = () => {
    const enteredBody = bodyRef.current?.getInstance().getMarkdown();

    axios(`${process.env.REACT_APP_URL}/api/posts`, {
      method: "POST",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
      data: {
        title: title,
        body: enteredBody,
        startDate: startDate,
        endDate: endDate,
        location: location,
        totalCount: totalCount,
        closeDate: closeDate,
      },
    })
      .then((res) => {
        if (res.headers.access_hh) {
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
        }
        navigate(`/main`);
        window.location.reload();
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
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
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
      });
  };

  return (
    <>
      <PageContainer>
        <Container>
          <div>
            <div>
              <input
                id="title"
                placeholder="제목입력"
                type="text"
                required
                onChange={(e) => {
                  setTitle(e.target.value);
                }}
              />
            </div>
            <div className="container">
              <div>
                <span>여행 출발 날짜 </span>
                <input
                  className="date"
                  type="date"
                  required
                  onChange={(e) => {
                    setStartDate(e.target.value);
                  }}
                  min={`${year}-${("0" + month).slice(-2)}-${date}`}
                />
              </div>
              <div>
                <span>여행 종료? 날짜 </span>
                <input
                  className="date"
                  type="date"
                  required
                  onChange={(e) => {
                    setEndDate(e.target.value);
                  }}
                  min={startDate}
                />
              </div>
              <div>
                <span>모집 마감 </span>
                <input
                  type="date"
                  required
                  onChange={(e) => {
                    setCloseDate(e.target.value);
                  }}
                  min={`${year}-${("0" + month).slice(-2)}-${date}`}
                  max={startDate}
                />
                <span>까지</span>
              </div>
              <div>
                <span>여행지 </span>
                <input
                  type="text"
                  required
                  onChange={(e) => {
                    setLocation(e.target.value);
                  }}
                />
              </div>
              <div>
                <span>본인을 포함한 총 모집 인원 수 </span>
                <input
                  className="number"
                  type="number"
                  required
                  min="2"
                  max="20"
                  onChange={(e) => {
                    setTotalCount(e.target.value);

                    if (e.target.value > 20) {
                      e.target.value = 20;
                      setTotalCount(20);
                    }
                  }}
                />
              </div>
            </div>
          </div>
        </Container>
        <Wrapper>
          <Editor
            placeholder="10글자 이상 작성해주세요."
            required
            ref={bodyRef}
            previewStyle="vertical" // 미리보기 스타일 지정
            height="300px" // 에디터 창 높이
            initialEditType="markdown" // 초기 입력모드 설정(디폴트 markdown)
            hideModeSwitch={true}
            toolbarItems={[
              // 툴바 옵션 설정
              ["heading", "bold", "italic", "strike"],
              ["hr", "quote"],
              ["ul", "ol", "task", "indent", "outdent"],
              ["table", "image", "link"],
              ["code", "codeblock"],
            ]}
            hooks={{
              addImageBlobHook: (blob, callback) => {
                const formData = new FormData();
                formData.append("image", blob);
                axios(`${process.env.REACT_APP_URL}/api/images/upload`, {
                  method: "POST",
                  headers: {
                    "Content-Type": "multipart/form-data",
                    access_hh: sessionStorage.getItem("AccessToken"),
                  },
                  data: formData,
                })
                  .then((res) => {
                    // 기홍님의 잔재.....
                    // let testid = res.data.imageId;
                    // setImageId(testid);
                    // mount.current = true;
                    callback(res.data.imageUrl);
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
                    } else if (err.response.status === 0)
                      alert(
                        "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
                      );
                    else {
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
                  });
              },
            }}
            plugins={[colorSyntax]} // colorSyntax 플러그인 적용
          ></Editor>
          <Button
            onClick={() => {
              submitHandler();
            }}
          >
            작성 완료
          </Button>
          <Button
            onClick={() => {
              navigate(`/main`);
            }}
          >
            취소
          </Button>
        </Wrapper>
      </PageContainer>
    </>
  );
};

export default NewPost;
const PageContainer = styled.div`
  #title {
    width: 30rem;
    height: 2rem;
    text-align: center;
  }
  // Break Point
  $tablet: 768px;
  $laptop: 1020px;
  $desktop: 1400px;

  // Mixins
  @mixin tablet {
    @media (min-width: #{$tablet}) {
      @content;
    }
  }

  @mixin laptop {
    @media (min-width: #{$laptop}) {
      @content;
    }
  }

  @mixin desktop {
    @media (min-width: #{$desktop}) {
      @content;
    }
  }
`;

const Container = styled.div`
  .container {
    display: flex;
    flex: 1;
    justify-content: space-around;
    width: 80%;
  }
`;
const Wrapper = styled.div`
  width: 70%;
`;
