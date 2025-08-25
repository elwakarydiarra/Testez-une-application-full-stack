const path = require('path')

module.exports = {
  devtool: 'inline-source-map',
  module: {
    rules: [
      {
        test: /\.[jt]s$/,
        include: [path.join(__dirname, '..', 'src')],
        exclude: [
          /node_modules/,
          /\.(e2e|spec)\.ts$/,
          /(ngfactory|ngstyle)\.js/,
        ],
        enforce: 'post',
        loader: '@jsdevtools/coverage-istanbul-loader',
        options: { esModules: true }
      }
    ]
  }
}
