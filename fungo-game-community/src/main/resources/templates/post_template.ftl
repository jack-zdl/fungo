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
		max-width: 80%;
	  display: flex;
		box-sizing: border-box;
		justify-content: center;
	  margin: 1em auto;
	  box-shadow: 0px 0px 7px 0px rgba(172, 172, 172, 0.5);
	  border-radius: 4px;
	  height: 80px;
	  padding: 10px 20px;
	  cursor: pointer;
	}
	.game img {
	  width: 60px;
	  height: 60px;
	  border-radius: 10px;
	  margin: 0;
		cursor: auto;
	}
	.game .score {
	  line-height: 60px;
	  color: #242529;
	}
	.game .score .num {
	  font-size: 16px;
	  font-weight: bold;
	}
	.game .score .unit {
	  font-size: 12px;
	  margin-left: 4px;
	}
	.game .info {
	  padding: 9px 10px;
	  flex-grow: 1;
	  overflow: hidden;
	}
	.game .info .name {
	  color: #242529;
	  font-size: 16px;
	  line-height: 16px;
	  white-space: nowrap;
	  text-overflow: ellipsis;
	  overflow: hidden;
	}
	.game .info .category {
	  color: #12a192;
	  font-size: 12px;
	  margin-top: 8px;
	}

    </style>
  </head>
<body>
    ${origin}
</body>
</html>