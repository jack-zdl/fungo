<!DOCTYPE html>
<html lang="en">
<head>
	<title>${title}</title>
    <meta charset="UTF-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1.0, maximum-scale=1.0,  user-scalable=0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <style>
        img {
            max-width: 100%;
        }
        body {
            padding: 12px 4%;
            margin: 0;
        }
        p {
            line-height: 27px;
            font-size: 17px;
            color: #242529;
            margin: 12px 0;
            word-wrap: break-word;
        }
        .game {
            max-width: 100%;
            display: flex;
            box-sizing: border-box;
            align-items:center;
            margin: 1em auto;
            border-radius: 25px;
            height: 50px;
            padding: 0px 15px 0px 5px;
            background-color: #EAFAF8;
            color:#3ACEC1;
            cursor: pointer;
        }
        .game img {
            width: 43px;
            height: 43px;
            border-radius: 50%;
            margin: 0;
            cursor: auto;
        }
        .game .score {

        }
        .game .score .num {
            font-size: 18px;
            font-weight: bold;
        }
        .game .score .unit {
            color:#999999;
            font-size: 12px;
            margin-left: 4px;
        }
        .game .info {
            padding: 0px 10px;
            flex-grow: 1;
            overflow: hidden;
        }
        .game .info .name {
            font-size: 14px;
            line-height: 14px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            font-weight: bold;
        }
        .game .info .category {
            display:inline-block;
            background-color:#fff;
            font-size: 10px;
            margin-top: 3px;
            padding:0px 8px;
            border-radius: 8px;
        }
    </style>
  </head>
<body>
    ${origin}
</body>
</html>