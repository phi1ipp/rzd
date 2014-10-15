<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>
    
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

            <xsl:apply-templates select="//div[@id='WaitStatus']/following-sibling::div[last()]//tr[@class='route-data']"/>
            <xsl:apply-templates select="//div[@id='WaitStatus']/following-sibling::div[last()]//table[@class='traindata']"/>
            <xsl:apply-templates select="//div[@id='WaitStatus']/following-sibling::div[last()]//div[@class='money']"/>

            <xsl:for-each select="//div[@class='order-cont']">
                <xsl:variable name="tsloop" select="number($timestamp)+position()"/>
                <Blank>
                    <xsl:attribute name="ID">
                        <xsl:value-of select="$tsloop"/>
                    </xsl:attribute>
                    <xsl:attribute name="PrevID">0</xsl:attribute>

                    <RetFlag>0</RetFlag>
                    <Amount><xsl:value-of select="$price"/></Amount>
                    <RegTime><xsl:value-of select="$ordertime"/></RegTime>
                    <TicketNum><xsl:value-of select=".//td[@class='topinfo-ticketnum']"/></TicketNum>
                    <AmountNDS>
                        <xsl:value-of select="substring-before(substring-after(.//table[@class='trainfin']//td[@class='trainfin-sum']//td[contains(text(),'Цена')]/following-sibling::td/text()[last()],'НДС '),')')"/>
                    </AmountNDS>
                    <TariffType>
                        <xsl:value-of select="substring-before(.//table[@class='trainfin']//td[@class='trainfin-type'],' ')"/>
                    </TariffType>
                    <PrintFlag>0</PrintFlag>
                    <RemoteCheckIn>0</RemoteCheckIn>
                    <RzhdStatus>0</RzhdStatus>
                </Blank>
                <Passenger>
                    <xsl:attribute name="ID">
                        <xsl:value-of select="$tsloop"/>
                    </xsl:attribute>
                    <xsl:attribute name="BlankID">
                        <xsl:value-of select="$tsloop"/>
                    </xsl:attribute>
                    <DocType>
                        <xsl:value-of select="substring-before(normalize-space(.//div[@class='boarding__pass-data']//div[@class='boarding__pass-doc']),' ')"/>
                    </DocType>
                    <DocNum>
                        <xsl:value-of select="substring-after(normalize-space(.//div[@class='boarding__pass-data']//div[@class='boarding__pass-doc']/text()[position()=1]),' ')"/>
                    </DocNum>
                    <Name>
                        <xsl:value-of select="normalize-space(.//div[@class='boarding__pass-data']//h4)"/>
                    </Name>
                    <Place>
                        <xsl:value-of select="substring-before(normalize-space(.//table[@class='traindata']//td[@class='traindata-pldat']/text()),' ')"/>
                    </Place>
                    <PlaceTier>
                        <xsl:variable name="tier" select="substring-after(normalize-space(.//table[@class='traindata']//td[@class='traindata-pldat']/text()),' ')"/>
                        <xsl:choose>
                            <xsl:when test="$tier='НИЖНЕЕ'">Н</xsl:when>
                            <xsl:when test="$tier='ВЕРХНЕЕ'">В</xsl:when>
                            <xsl:otherwise>С</xsl:otherwise>
                        </xsl:choose>
                    </PlaceTier>
                    <Type>
                        <xsl:variable name="tariff" select="substring-before(.//td[@class='trainfin-type']/text(),' ')"/>
                        <xsl:choose>
                            <xsl:when test="$tariff='Полный'">ПЛ</xsl:when>
                            <xsl:when test="$tariff='Детский'">ДТ</xsl:when>
                            <xsl:otherwise>БС</xsl:otherwise>
                        </xsl:choose>
                    </Type>
                </Passenger>
            </xsl:for-each>
        </UFS_RZhD_Gate>
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
                <xsl:when test="$cartype='Плац'">П</xsl:when>
                <xsl:when test="$cartype='Люкс'">Л</xsl:when>
                <xsl:when test="$cartype='Мягкий'">М</xsl:when>
                <xsl:when test="$cartype='Общий'">О</xsl:when>
                <xsl:when test="$cartype='Сидячий'">С</xsl:when>
                <xsl:otherwise>Н</xsl:otherwise>
            </xsl:choose>
        </CarType>
    </xsl:template>
    <xsl:template match='//tr[@class="route-data"]'>
        <xsl:variable name="year" select="substring-after(//div[@class='boarding__pass-data-add']/p[contains(.,'Год')],': ')"/>
        <ServiceClass>
            <xsl:value-of select="td[last()]/div/b/text()"/>
        </ServiceClass>
        <DepartTime>
            <xsl:value-of select="concat(normalize-space(td[position()=1]//td[@class='route-date']),'.')"/>
            <xsl:value-of select="concat($year,' ')"/>
            <xsl:value-of select="concat(normalize-space(td[position()=1]//td[@class='route-time']),':00')"/>
        </DepartTime>
        <ArrivalTime>
            <xsl:value-of select="concat(normalize-space(./td[last()-1]//td[@class='route-date']),'.')"/>
            <xsl:value-of select="concat($year,' ')"/>
            <xsl:value-of select="concat(normalize-space(./td[last()-1]//td[@class='route-time']), ':00')"/>
        </ArrivalTime>
        <StationFrom>
            <xsl:attribute name="Code">1</xsl:attribute>
            <xsl:value-of select="td[@class='route-points']//tr[position()=1]/td[position()=1]/text()"/>
        </StationFrom>
        <StationTo>
            <xsl:attribute name="Code">1</xsl:attribute>
            <xsl:value-of select="substring-after(td[@class='route-points']//tr[position()=1]/td[last()]/text(),'> ')"/>
        </StationTo>
    </xsl:template>
</xsl:stylesheet>