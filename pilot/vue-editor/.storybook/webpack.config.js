const path = require('path');
const rootPath = path.resolve(__dirname, '../src')

const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');

module.exports = (storybookBaseConfig, configType, defaultConfig) => {
  defaultConfig.resolve.extensions.push('.ts', '.tsx', '.vue', '.css', '.less', '.scss', '.sass', '.html');

  defaultConfig.module.rules.push({
    test: /\.ts$/,
    exclude: /node_modules/,
    use: [
      {
        loader: 'ts-loader',
        options: {
          appendTsSuffixTo: [/\.vue$/],
          transpileOnly: true // used with ForkTsCheckerWebpackPlugin
        },
      }
    ],
  });

  defaultConfig.module.rules.push({
    test: /\.scss$/,
    use: [
      'style-loader',
      'css-loader',
      {
        loader: 'sass-loader',
        options: {
          data: `
		    @import "@/styles/_mixins.scss";
			@import "@/styles/_variables.scss";
		  `
        }
      }
    ],
    include: path.resolve(__dirname, '../'),
  });

  defaultConfig.resolve.alias['@'] = rootPath
  defaultConfig.resolve.alias['~'] = rootPath

  defaultConfig.plugins.push(new ForkTsCheckerWebpackPlugin());

  return defaultConfig;
};
