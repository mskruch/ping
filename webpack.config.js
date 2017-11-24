const path = require('path');
const root = path.resolve(__dirname, 'src/main/webapp');
const src = path.resolve(root, 'javascript');
const dest = path.resolve(__dirname, 'src/main/webapp/dist');
const webpack = require('webpack');


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
                        presets: ['react', 'stage-2']
                    }
                }
            }
        ]
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery',
            Popper: ['popper.js', 'default']
        })
    ]
};