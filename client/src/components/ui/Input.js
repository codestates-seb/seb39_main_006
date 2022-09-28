import styled from "styled-components";

const Input = () => {
  return (
    <StyledInput className="input-tag">
      <div className="container">
        <input
          title="&lt; /&gt;"
          labl="Label"
          pholder="Placeholder"
          idn="none"
        />
        {/* <input
          title="&amp; : hover"
          labl="Label"
          pholder="Placeholder"
          idn="hover"
        />
        <input
          title="&amp; : focus"
          labl="Label"
          pholder="Placeholder"
          idn="focus"
        /> */}
      </div>
    </StyledInput>
  );
};
export default Input;
const StyledInput = styled.div`
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
`;
