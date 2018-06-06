<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
    
    <xsl:template match="/pdfData">
        <html>
            <head>
                <title>
                    Balance
                </title>
                
                <style type="text/css">
                    .flow-box {
                        max-width: 800px;
                        margin: auto;
                        padding: 30px;
                        border: 1px solid #eee;
                        box-shadow: 0 0 10px rgba(0, 0, 0, .15);
                        font-size: 16px;
                        line-height: 24px;
                        font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
                        color: #555;
                    }

                    .flow-box table {
                        width: 100%;
                        line-height: inherit;
                        text-align: left;
                    }

                    .flow-box table td {
                        padding: 5px;
                        vertical-align: top;
                    }

                    .flow-box table tr td:nth-child(2) {
                        text-align: right;
                    }

                    .flow-box table tr.top table td {
                        padding-bottom: 20px;
                    }

                    .flow-box table tr.top table td.title {
                        font-size: 45px;
                        line-height: 45px;
                        color: #333;
                    }

                    .flow-box table tr.information table td {
                        padding-bottom: 40px;
                    }

                    .flow-box table tr.heading td {
                        background: #eee;
                        border-bottom: 1px solid #ddd;
                        font-weight: bold;
                    }

                    .flow-box table tr.invoice td{
                        border-bottom: 1px solid #eee;
                    }

                    .flow-box table tr.total td {
                        border-bottom: none;
                    }

                    @media only screen and (max-width: 600px) {
                        .flow-box table tr.top table td {
                            width: 100%;
                            display: block;
                            text-align: center;
                        }

                        .flow-box table tr.information table td {
                            width: 100%;
                            display: block;
                            text-align: center;
                        }
                    }

                    /** RTL **/
                    .rtl {
                        direction: rtl;
                        font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
                    }

                    .rtl table {
                        text-align: right;
                    }

                    .rtl table tr td:nth-child(2) {
                        text-align: left;
                    }
                </style>
            </head>

            <body>
                <div class="flow-box">
                    <table cellpadding="0" cellspacing="0">
                        <tr class="top">
                            <td colspan="2">
                                <table>
                                    <tr>
                                        <td class="title">
                                            <img src="CIA-persistence/src/main/resources/LizzardCorpLogo.png" style="width:100%; max-width:300px;"/>
                                        </td>
                                        <td>
                                            <xsl:apply-templates select="balance" mode="head"/>
                                            <xsl:apply-templates select="income" mode="head"/>
                                            <xsl:apply-templates select="expense" mode="head"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <xsl:apply-templates select="balance"/>
                        <xsl:apply-templates select="income"/>
                        <xsl:apply-templates select="expense"/>
                    </table>
                </div>          
            </body>
        </html>
    </xsl:template>

    <xsl:template match="balance" mode="head">
        <td>
            Balance
            <xsl:apply-templates select="../range"/>
        </td>
    </xsl:template>
    <xsl:template match="income" mode="head">
        <td>
            Incomes
            <xsl:apply-templates select="../range"/>
        </td>
    </xsl:template>
    <xsl:template match="expense" mode="head">
        <td>
            Expenses
            <xsl:apply-templates select="../range"/>
        </td>
    </xsl:template>

    <xsl:template match="range">
        <br/> From: <xsl:value-of select="from"/>
        <br/> To: <xsl:value-of select="to"/>
    </xsl:template>

    <xsl:template match="balance">
        <tr class="heading">
            <td>Incomes</td>     
            <td>Amount</td>
        </tr>
        <xsl:apply-templates select="flowItem/invoice[@type = 'income']" mode="income"/>
        <tr class="heading">
            <td>Expenses</td>     
            <td>Amount</td>
        </tr>
        <xsl:apply-templates select="flowItem/invoice[@type = 'expense']" mode="expense"/>
        <tr class="heading">
            <td><b>Total:</b></td>
            <td><xsl:value-of select="format-number(sum(flowItem/invoice[@type = 'income']/totalPrice) - sum(flowItem/invoice[@type = 'expense']/totalPrice), '#.00')"/>,-</td>
        </tr>
    </xsl:template>

    <xsl:template match="income">
        <tr class="heading">
            <td>Incomes</td>     
            <td>Amount</td>
        </tr>
        <xsl:apply-templates select="flowItem/invoice[@type = 'income']" mode="income"/>
        <tr class="heading">
            <td><b>Total:</b></td>
            <td><xsl:value-of select="format-number(sum(flowItem/invoice[@type = 'income']/totalPrice), '#.00')"/>,-</td>
        </tr>
    </xsl:template>

    <xsl:template match="expense">
        <tr class="heading">
            <td>Expenses</td>     
            <td>Amount</td>
        </tr>
        <xsl:apply-templates select="flowItem/invoice[@type = 'expense']" mode="expense"/>
        <tr class="heading">
            <td><b>Total:</b></td>
            <td><xsl:value-of select="format-number(sum(flowItem/invoice[@type = 'expense']/totalPrice), '#.00')"/>,-</td>
        </tr>
    </xsl:template>

    <xsl:template match="invoice" mode="income">
        <tr class="invoice">
            <td>
                <xsl:value-of select="../person[1]/name"/>
                <br/>
                <xsl:value-of select="../person[1]/accountNumber"/>
            </td>
            <td>
                <xsl:value-of select="totalPrice"/>,-
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="invoice" mode="expense">
        <tr class="invoice">
            <td>
                <xsl:value-of select="../person[2]/name"/>
                <br/>
                <xsl:value-of select="../person[2]/accountNumber"/>
            </td>
            <td>
                <xsl:value-of select="totalPrice"/>,-
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
