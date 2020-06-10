<%@page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="cellWidth" value="135"/>
<c:set var="cellHeight" value="36"/>
<c:set var="width" value="${(param.col + 3) * cellWidth}"/>
<c:set var="height" value="${40 + 30 + 48 + (param.row + 1) * cellHeight}"/>

<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.5.0.final using JasperReports Library version 6.5.0  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd"
		name="qqqqqqqqqqqq" uuid="69d0702d-de28-4767-9326-de34443a2cac"
		pageWidth="${width + 32 * 2}" pageHeight="${height + 20 * 2}" columnWidth="${width}"
		leftMargin="32" rightMargin="32" topMargin="20" bottomMargin="20">
	<property name="com.jaspersoft.studio.data.defaultdataadapter" value="One Empty Record"/>
	<property name="com.jaspersoft.studio.unit.width" value="px"/>
	<property name="com.jaspersoft.studio.unit.height" value="px"/>
	<property name="com.jaspersoft.studio.unit.y" value="px"/>
	<property name="net.sf.jasperreports.export.xls.detect.cell.type" value="true"/>
	
	<style name="BaseStyle" isDefault="true" forecolor="#454545" backcolor="#FFFFFF" vTextAlign="Middle" fontName="微软雅黑" fontSize="16"/>
	<style name="TableHeaderStyle" style="BaseStyle" mode="Opaque" forecolor="#FFFFFF" backcolor="#888888" hTextAlign="Left" markup="styled" fontSize="16">
		<box>
			<pen lineStyle="Solid" lineColor="#666666"/>
			<topPen lineWidth="0.5"/>
		</box>
	</style>
	<style name="TableFooterStyle" style="BaseStyle">
		<box>
			<bottomPen lineWidth="0.5"/>
		</box>
	</style>
	<style name="RowColorStyle" style="BaseStyle" mode="Opaque">
		<conditionalStyle>
			<conditionExpression><![CDATA[$V{REPORT_COUNT}%2 == 0]]></conditionExpression>
			<style backcolor="#F2F2F2"/>
		</conditionalStyle>
	</style>
	
	<parameter name="title" class="java.lang.String"/>
	<parameter name="annotation" class="java.lang.String"/>
	<parameter name="xAxis" class="java.lang.String[]"/>
	<parameter name="yAxis" class="java.lang.String[]"/>
	
	<c:forEach var="idx" begin="0" end="${param.col - 1}">
		<field name="data_col${idx}" class="java.lang.Float"/>
	</c:forEach>
	
	<variable name="data_row_sum" class="java.lang.Float" resetType="Column">
		<variableExpression><![CDATA[($F{data_col0} != null ? $F{data_col0} : 0)<c:forEach
				var="idx" begin="1" end="${param.col - 1}">+($F{data_col${idx}} != null ? $F{data_col${idx}} : 0)</c:forEach>]]></variableExpression>
		<initialValueExpression><![CDATA[0]]></initialValueExpression>
	</variable>
	<variable name="data_row_sum_sum" class="java.lang.Float" calculation="Sum">
		<variableExpression><![CDATA[$V{data_row_sum}]]></variableExpression>
	</variable>
	<c:forEach var="idx" begin="0" end="${param.col - 1}">
		<variable name="data_col${idx}_sum" class="java.lang.Float" calculation="Sum">
			<variableExpression><![CDATA[$F{data_col${idx}}]]></variableExpression>
			<initialValueExpression><![CDATA[0]]></initialValueExpression>
		</variable>
	</c:forEach>
	
	<!--queryString>
		<![CDATA[]]>
	</queryString>
	<background>
		<band splitType="Stretch"/>
	</background-->
	
	<title>
		<band height="70" splitType="Stretch">
			<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.FreeLayout"/>
			<textField>
				<reportElement x="0" y="0" width="${width}" height="40" forecolor="#454545" uuid="e0e8fc87-d233-4d08-9043-47db853ba43b"/>
				<textElement textAlignment="Center">
					<font size="24"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="0" y="40" width="${width}" height="30" uuid="564e2364-6f15-4c51-a35f-06633c0d19f8"/>
				<textElement textAlignment="Right"/>
				<textFieldExpression><![CDATA[$P{annotation}]]></textFieldExpression>
			</textField>
		</band>
	</title>
	
	<columnHeader>
		<band height="48" splitType="Stretch">
			<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.HorizontalRowLayout"/>
			<textField>
				<reportElement style="TableHeaderStyle" x="0" y="0" width="${cellWidth}" height="48" uuid="c62e6f9a-d034-4401-8811-c06c7915bd6a"/>
				<box>
					<leftPen lineWidth="0.5" lineColor="#000000"/>
				</box>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA["序号"]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement style="TableHeaderStyle" x="${cellWidth}" y="0" width="${cellWidth}" height="48" uuid="a7bbeb21-64c9-4ee4-baa4-e4df6ba82f00"/>
				<textFieldExpression><![CDATA["日期"]]></textFieldExpression>
			</textField>
			<c:forEach var="idx" begin="0" end="${param.col - 1}">
				<textField>
					<reportElement style="TableHeaderStyle" x="${cellWidth * (idx + 2)}" y="0" width="${cellWidth}" height="48" uuid="48a38cf5-e3f4-4504-ba9c-e4a5ed8dd604"/>
					<textFieldExpression><![CDATA[$P{xAxis}[${idx}] ]]></textFieldExpression>
				</textField>
			</c:forEach>
			<textField>
				<reportElement style="TableHeaderStyle" x="${cellWidth * (param.col + 2)}" y="0" width="${cellWidth}" height="48" uuid="89c53d2b-11a4-4e6c-b988-2c6cefe1f929"/>
				<box>
					<rightPen lineWidth="0.5" lineColor="#000000"/>
				</box>
				<textFieldExpression><![CDATA["合计"]]></textFieldExpression>
			</textField>
		</band>
	</columnHeader>
	
	<detail>
		<band height="${cellHeight}" splitType="Stretch">
			<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.VerticalRowLayout"/>
			<frame>
				<reportElement key="" style="RowColorStyle" x="0" y="0" width="${width}" height="${cellHeight}" uuid="0490e8f6-9e5a-418c-a4e9-8ec8437a71b7">
					<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.HorizontalRowLayout"/>
				</reportElement>
				<textField>
					<reportElement x="0" y="0" width="${cellWidth}" height="${cellHeight}" uuid="8b5f2957-cf21-468b-b9a9-91ad231053c2"/>
					<box>
						<leftPen lineWidth="0.5"/>
					</box>
					<textElement textAlignment="Center"/>
					<textFieldExpression><![CDATA[$V{REPORT_COUNT}]]></textFieldExpression>
				</textField>
				<textField>
					<reportElement x="${cellWidth}" y="0" width="${cellWidth}" height="${cellHeight}" uuid="adbfd5fa-e6cd-4438-8307-1a67380d6157"/>
					<textFieldExpression><![CDATA[$P{yAxis}[$V{REPORT_COUNT} - 1] ]]></textFieldExpression>
				</textField>
				<c:forEach var="idx" begin="0" end="${param.col - 1}">
					<textField>
						<reportElement x="${cellWidth * (idx + 2)}" y="0" width="${cellWidth}" height="${cellHeight}" uuid="6cc68bb5-9684-405a-803e-ab5bab7de275"/>
						<textFieldExpression><![CDATA[$F{data_col${idx}} != null ? $F{data_col${idx}} : "--"]]></textFieldExpression>
					</textField>
				</c:forEach>
				<textField>
					<reportElement x="${cellWidth * (param.col + 2)}" y="0" width="${cellWidth}" height="${cellHeight}" uuid="02ae1304-452c-4430-9d4f-4450fdb61fb1"/>
					<box>
						<rightPen lineWidth="0.5"/>
					</box>
					<textFieldExpression><![CDATA[new BigDecimal($V{data_row_sum}).setScale(2,BigDecimal.ROUND_HALF_UP)]]></textFieldExpression>
				</textField>
			</frame>
		</band>
	</detail>
	
	<summary>
		<band height="${cellHeight}" splitType="Stretch">
			<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.HorizontalRowLayout"/>
			<textField>
				<reportElement style="TableFooterStyle" x="0" y="0" width="${cellWidth}" height="${cellHeight}" uuid="7fcfd8fa-2882-47ce-bce5-f1d1ae12eb31"/>
				<box>
					<leftPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA[""]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement style="TableFooterStyle" x="${cellWidth}" y="0" width="${cellWidth}" height="${cellHeight}" uuid="97cdd36e-2cb5-4801-b22c-27c20f7d5376"/>
				<textFieldExpression><![CDATA["小计"]]></textFieldExpression>
			</textField>
			<c:forEach var="idx" begin="0" end="${param.col - 1}">
				<textField>
					<reportElement style="TableFooterStyle" x="${cellWidth * (idx + 2)}" y="0" width="${cellWidth}" height="${cellHeight}" uuid="ff2450ff-5fe5-49c0-b3f5-9d04027fde88"/>
					<textFieldExpression><![CDATA[$V{data_col${idx}_sum} != null ? new BigDecimal($V{data_col${idx}_sum}).setScale(2,BigDecimal.ROUND_HALF_UP) : 0]]></textFieldExpression>
				</textField>
			</c:forEach>
			<textField>
				<reportElement style="TableFooterStyle" x="${cellWidth * (param.col + 2)}" y="0" width="${cellWidth}" height="${cellHeight}" uuid="027b11a0-0614-4da9-8239-57babfca76db"/>
				<box>
					<rightPen lineWidth="0.5"/>
				</box>
				<textFieldExpression><![CDATA[new BigDecimal($V{data_row_sum_sum}).setScale(2,BigDecimal.ROUND_HALF_UP)]]></textFieldExpression>
			</textField>
		</band>
	</summary>
</jasperReport>
