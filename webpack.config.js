const path = require('path');
const root = path.resolve(__dirname, 'src/main/webapp');
const src = path.resolve(root, 'javascript');
const dest = path.resolve(__dirname, 'src/main/webapp/dist');


module.exports = {
    entry: src + '/index.jsx',
    devtool: 'sourcemaps',
    output: {
        filename: 'bundle.js',
        path: dest
    },
    resolve: {
        extensions: ['.js', '.jsx']
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                // test: /\.jsx?$/,
                exclude: /(node_modules)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['react','stage-2']
                    }
                }
            }
        ]
    }
};