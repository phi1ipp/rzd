<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="orderNum" select="0"/>
    <xsl:param name="timestamp" select="0"/>
    <xsl:param name="terminal" select="XX"/>
    <xsl:template match="/">
        <xsl:variable name="price"
                      select="normalize-space(//td[@class='trainfin-sum']//tr/td[text()[contains(.,'Цена')]]/following-sibling::td/text())"/>
        <xsl:variable name="ordertime" select="//div[@class='money']//tr/td[text()[contains(.,'оформления')]]/following-sibling::td/text()"/>

        <UFS_RZhD_Gate>
            <TransID><xsl:value-of select="$timestamp"/></TransID>
            <PrevTransID>0</PrevTransID>
            <LastRefundTransID>0</LastRefundTransID>
            <STAN><xsl:value-of select="$timestamp"/></STAN>
            <TStatus>0</TStatus>
            <RStatus>0</RStatus>
            <OrderNum><xsl:value-of select="$orderNum"/></OrderNum>
            <Type>1</Type>
            <Fee>0</Fee>
            <Phone></Phone>
            <Email></Email>
            <PlaceCount>1</PlaceCount>
            <GenderClass>0</GenderClass>
            <GroupDirection>0</GroupDirection>
            <IsTest>0</IsTest>
            <Domain>ufs-online.ru</Domain>
            <PayTypeID>CASH</PayTypeID>
            <Terminal><xsl:value-of select="$terminal"/></Terminal>
            <Amount><xsl:value-of select="$price"/></Amount>
            <CreateTime><xsl:value-of select="$ordertime"/></CreateTime>
            <ConfirmTime><xsl:value-of select="$ordertime"/></ConfirmTime>

            <xsl:apply-templates select='//tr[@class="route-data"]'/>
            <xsl:apply-templates select='//table[@class="traindata"]'/>
            <xsl:apply-templates select="//div[@class='money']"/>

            <Blank>
                <xsl:attribute name="ID">
                    <xsl:value-of select="$timestamp"/>
                </xsl:attribute>
                <xsl:attribute name="PrevID">0</xsl:attribute>

                <RetFlag>0</RetFlag>
                <Amount><xsl:value-of select="$price"/></Amount>
                <RegTime><xsl:value-of select="$ordertime"/></RegTime>
                <TicketNum><xsl:value-of select="//td[@class='topinfo-ticketnum']"/></TicketNum>
                <xsl:apply-templates select="//table[@class='trainfin']"/>
                <PrintFlag>0</PrintFlag>
                <RemoteCheckIn>0</RemoteCheckIn>
                <RzhdStatus>0</RzhdStatus>
            </Blank>
            <Passenger>
                <xsl:attribute name="ID">
                    <xsl:value-of select="$timestamp"/>
                </xsl:attribute>
                <xsl:attribute name="BlankID">
                    <xsl:value-of select="$timestamp"/>
                </xsl:attribute>
                <xsl:apply-templates select="//div[@class='boarding__pass-data']"/>
                <Place>
                    <xsl:value-of select="normalize-space(//table[@class='traindata']//td[@class='traindata-pldat'])"/>
                </Place>
                <Type>

                </Type>
            </Passenger>
        </UFS_RZhD_Gate>
    </xsl:template>

    <xsl:template match="//table[@class='trainfin']">
        <AmountNDS>
            <xsl:value-of select="substring-before(substring-after(//td[@class='trainfin-sum']//td[contains(text(),'Цена')]/following-sibling::td/text()[last()],'НДС '),')')"/>
        </AmountNDS>
        <TariffType>
            <xsl:value-of select="substring-before(//td[@class='trainfin-type'],' ')"/>
        </TariffType>
    </xsl:template>
    <xsl:template match="//div[@class='money']">
        <Carrier>
            <xsl:value-of select="normalize-space(substring-before(//tr/td[position()=1 and contains(text()[position()=1],'Перевозчик')]/following-sibling::td, '/'))"/>
        </Carrier>
        <TimeDescription>
            <xsl:value-of select="normalize-space(//tr/td[position()=1 and contains(text()[position()=1],'Дополнительная')]/following-sibling::td)"/>
        </TimeDescription>
    </xsl:template>
    <xsl:template match='//table[@class="traindata"]'>
        <TrainNum><xsl:value-of select="substring-before(normalize-space(.//td[@class='traindata-tdat']),' ')"/></TrainNum>
        <CarNum><xsl:value-of select="substring-before(normalize-space(.//td[@class='traindata-cdat']),' ')"/></CarNum>
        <xsl:variable name="cartype" select="substring-after(normalize-space(.//td[@class='traindata-cdat']),' ')"/>
        <CarType>
            <xsl:choose>
                <xsl:when test="$cartype='Купе'">К</xsl:when>
                <xsl:when test="$cartype='Плацкартный'">П</xsl:when>
                <xsl:when test="$cartype='Люкс'">Л</xsl:when>
                <xsl:when test="$cartype='Мягкий'">М</xsl:when>
                <xsl:when test="$cartype='Общий'">О</xsl:when>
                <xsl:when test="$cartype='Сидячий'">С</xsl:when>
                <xsl:otherwise>Н</xsl:otherwise>
            </xsl:choose>
        </CarType>
    </xsl:template>
    <xsl:template match='//tr[@class="route-data"]'>
        <ServiceClass>
            <xsl:value-of select="td[last()]/div/b/text()"/>
        </ServiceClass>
        <DepartTime>
            <xsl:value-of select="concat(normalize-space(td[position()=1]//td[@class='route-date']),' ')"/>
            <xsl:value-of select="normalize-space(td[position()=1]//td[@class='route-time'])"/>
        </DepartTime>
        <ArrivalTime>
            <xsl:value-of select="concat(normalize-space(td[last()]/preceding-sibling::td//td[@class='route-date']),' ')"/>
            <xsl:value-of select="normalize-space(td[last()]/preceding-sibling::td//td[@class='route-time'])"/>
        </ArrivalTime>
        <StationFrom>
            <xsl:value-of select="td[@class='route-points']//tr/td[position()=1]"/>
        </StationFrom>
        <StationTo>
            <xsl:value-of select="substring-after(td[@class='route-points']//tr/td[last()],'> ')"/>
        </StationTo>
    </xsl:template>
    <xsl:template match="//div[@class='boarding__pass-data']">
        <DocType>
            <xsl:value-of select="substring-before(normalize-space(//div[@class='boarding__pass-doc']),' ')"/>
        </DocType>
        <DocNum>
            <xsl:value-of select="substring-after(normalize-space(//div[@class='boarding__pass-doc']/text()[position()=1]),' ')"/>
        </DocNum>
        <Name>
            <xsl:value-of select="normalize-space(//h4)"/>
        </Name>
    </xsl:template>
</xsl:stylesheet>