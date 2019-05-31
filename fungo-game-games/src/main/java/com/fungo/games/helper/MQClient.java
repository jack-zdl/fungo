package com.fungo.games.helper;

import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
//@Component
public class MQClient {

    private static final Logger logger = LoggerFactory.getLogger(MQClient.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private GameReleaseLogService gameReleaseLogService;

    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    @Autowired
    private IEvaluateService iEvaluateService;

    @Autowired
    private MQProduct mqProduct;

    @Autowired
    private IGameService iGameService;




    /**
     * 更新游戏
     * @param gameDto
     * @param message
     * @param deliveryTag
     * @param channel
     */
    /*@RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_UPDATE)
    public void topicReceiveGameUpdate(@Payload GameDto gameDto, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
        System.out.println("mq消费信息" + gameDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        Game game = new Game();
                        BeanUtils.copyProperties(gameDto, game);
                        gameService.updateById(game);
                        channel.basicAck(envelope.getDeliveryTag(), false); //确认消费
                    }catch (Exception e) {
                        logger.error("游戏更新失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                        //channel.abort();  //此操作中的所有异常将被丢弃
                    }finally {
//                        channel.basicAck(envelope.getDeliveryTag(),false);
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }

    }*/

    /**
     * 插入游戏版本日志审批
     * @param gameReleaseLogDto
     * @param message
     * @param deliveryTag
     * @param channel
     */
    /*@RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_INSERT)
    public void topicReceiveGameInsert(@Payload GameReleaseLogDto gameReleaseLogDto, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_INSERT);
        System.out.println("mq消费信息" + gameReleaseLogDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        GameReleaseLog gameReleaseLog = new GameReleaseLog();
                        BeanUtils.copyProperties(gameReleaseLogDto, gameReleaseLog);
                        gameReleaseLogService.insert(gameReleaseLog);
                        channel.basicAck(envelope.getDeliveryTag(), true);
                    }catch (Exception e) {
                        logger.error("游戏版本日志审批插入失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                    }finally {
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }
    }*/


    /**
     * 新增游戏
     * @param gameDto
     * @param message
     * @param deliveryTag
     * @param channel
     */
    /*@RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_INSERT)
    public void topicReceiveGameReleaseLogInsert( @Payload GameDto gameDto, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_INSERT);
        System.out.println("mq消费信息" + gameDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        Game game = new Game();
                        BeanUtils.copyProperties(gameDto, game);
                        gameService.insert(game);
                        channel.basicAck(envelope.getDeliveryTag(), true);
                    }catch (Exception e) {
                        logger.error("游戏插入失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                    }finally {
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }
    }*/


    /**
     * 根据后台标签id集合，分类标签，游戏id
     * @param tags
     * @param categoryId
     * @param gameId
     * @param message
     * @param deliveryTag
     * @param channel
     */
    /*@RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_INSERT)
    public void topicReceiveAddGameTagInsert(@Payload List<String> tags,@Payload String categoryId,@Payload String gameId, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_INSERT);
//        System.out.println("mq消费信息" + gameDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {

                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        boolean flag = iEvaluateService.feignAddGameTagInsert(tags,categoryId,gameId);
                        if (flag){
                            channel.basicAck(envelope.getDeliveryTag(), true);
                        }else{
//                            最多添加3个标签
                            logger.error("最多添加3个标签");
                            channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                        }
                    }catch (Exception e) {
                        logger.error("根据后台标签id集合，分类标签，游戏id 执行失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                    }finally {
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }
    }*/


    /**
     * 游戏评价邀请记录添加
     * @param map
     * @param deliveryTag
     * @param channel
     */
    /*@RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_INSERT)
    public void topicGameInviteInsert(@Payload Map<String,String> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_INSERT);
//        System.out.println("mq消费信息" + gameDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
//                        boolean flag = gameService.feignAddGameTagInsert(tags,categoryId,gameId);
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        GameInviteDto gameInvite1 = JSON.parseObject(map.get("gameInvite"), GameInviteDto.class);
                        String appVersion = JSON.parseObject(map.get("appVersion"), String.class);

                        GameInvite gameInvite = new GameInvite();
                        BeanUtils.copyProperties(gameInvite1, gameInvite);
                        gameInvite.insert();
//                            Mqfeign调用
//                        iEvaluateProxyService.push(gameInvite.getInviteMemberId(), 3, appVersion);
//                        插入消息记录
                        mqProduct.push(gameInvite.getInviteMemberId(), 3, appVersion);
                    }catch (Exception e) {
                        logger.error("gameInvite.insert失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                    }finally {
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }
    }*/

    /**
     * 计数器
     * @param map
     * @param deliveryTag
     * @param channel
     */
    /*@RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_INSERT)
    public void updateCounter(@Payload Map<String,String> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_INSERT);
//        System.out.println("mq消费信息" + gameDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
//                        boolean flag = gameService.feignAddGameTagInsert(tags,categoryId,gameId);
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        //        根据表名(动态)修改
                        iGameService.updateCountor(map);
                    }catch (Exception e) {
                        logger.error("updateCounter失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                    }finally {
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }
    }*/



}
