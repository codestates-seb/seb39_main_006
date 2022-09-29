import SignUpContainer from "../pages/SignUpContainer.js";
import LoginContainer from "../pages/LoginContainer.js";
import MainContainer from "../pages/MainContainer.js";
import SideContainer from "../pages/SideContainer.js";
const Routes2 = [
  {
    path: "/signup",
    element: <SignUpContainer />,
  },
  {
    path: "/login",
    element: <LoginContainer />,
  },
  {
    path: "/",
    element: <MainContainer />,
  },
  {
    path: "/question",
    element: <SideContainer />,
  },
];
export default Routes2;
