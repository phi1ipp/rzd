<?php
return array(
    'rootLogger' => array(
        'appenders' => array('default'),
    ),
    'appenders' => array(
        'default' => array(
            'class' => 'LoggerAppenderFile',
            'layout' => array(
                'class' => 'LoggerLayoutPattern'
            ),
            'params' => array(
                'file' => 'logs/my.log',
                'append' => true
            )
        )
    )
);
