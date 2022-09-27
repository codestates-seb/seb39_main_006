module.exports = {
  entry: "./src/index.js",
  output: {
    filename: "App.js",
    path: path.resolve(__dirname, "dist"),
  },
  module: {
    rules: [
      // CSS 파일 로더 설정
      {
        test: /\.css$/,
        include: [path.resolve(__dirname, "../src")], // Internal Styles
        use: [
          { loader: "isomorphic-style-loader" },
          {
            loader: "css-loader", // CSS Modules Enabled
            options: {
              importLoaders: 1,
              sourceMap: isDebug,
              modules: true,
              localIdentName: isDebug
                ? "[name]-[local]-[hash:base64:5]"
                : "[hash:base64:5]",
              minimize: !isDebug,
              discardComments: { removeAll: true },
            },
          },
          {
            loader: "postcss-loader",
            options: { config: "./tools/postcss.config.js" },
          },
        ],
      },
      {
        test: /\.css$/,
        exclude: [path.resolve(__dirname, "../src")], // External Styles
        use: [
          { loader: "isomorphic-style-loader" },
          {
            loader: "css-loader", // CSS Modules Disabled
            options: {
              sourceMap: isDebug,
              minimize: !isDebug,
              discardComments: { removeAll: true },
            },
          },
        ],
      },
    ],
  },
};
