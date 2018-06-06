<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
    
    <xsl:template match="/pdfData">
        <html>
            <head>
                <title>Invoice PDF</title>
                
                <style type="text/css">
                    .invoice-box {
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

                    .invoice-box table {
                        width: 100%;
                        line-height: inherit;
                        text-align: left;
                    }

                    .invoice-box table td {
                        padding: 5px;
                        vertical-align: top;
                    }

                    .invoice-box table tr td:nth-child(2) {
                        text-align: right;
                    }

                    .invoice-box table tr.top table td {
                        padding-bottom: 20px;
                    }

                    .invoice-box table tr.top table td.title {
                        font-size: 45px;
                        line-height: 45px;
                        color: #333;
                    }

                    .invoice-box table tr.information table td {
                        padding-bottom: 40px;
                    }

                    .invoice-box table tr.heading td {
                        background: #eee;
                        border-bottom: 1px solid #ddd;
                        font-weight: bold;
                    }

                    .invoice-box table tr.details td {
                        padding-bottom: 20px;
                    }

                    .invoice-box table tr.item td{
                        border-bottom: 1px solid #eee;
                    }

                    .invoice-box table tr.item.last td {
                        border-bottom: none;
                    }

                    .invoice-box table tr.total td:nth-child(2) {
                        border-top: 2px solid #eee;
                        font-weight: bold;
                    }

                    @media only screen and (max-width: 600px) {
                        .invoice-box table tr.top table td {
                            width: 100%;
                            display: block;
                            text-align: center;
                        }

                        .invoice-box table tr.information table td {
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
                <div class="invoice-box">
                    <table cellpadding="0" cellspacing="0">
                        <tr class="top">
                            <td colspan="2">
                                <table>
                                    <tr>
                                        <td class="title">
                                            <img src="CIA-persistence/src/main/resources/LizzardCorpLogo.png" style="width:100%; max-width:300px;"/>
                                        </td>
                                        <td>
                                            <xsl:apply-templates select="invoice" mode="head"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr class="information">
                            <td colspan="2">
                                <table>
                                    <tr>
                                        <td>
                                            <b>buyer:</b><br/>
                                            <xsl:apply-templates select="person[1]"/>
                                        </td>
                                        <td>
                                            <b>seller:</b><br/>
                                            <xsl:apply-templates select="person[2]"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <xsl:apply-templates select="invoice" mode="itemList"/>
                    </table>
                </div>          
            </body>
        </html>
    </xsl:template>

    <xsl:template match="invoice" mode="head">
        <td>
            Invoice #:
            <xsl:value-of select="@id"/>
            <br/>
            Created:
            <xsl:value-of select="issueDate"/>
            <br/>
            Due:
            <xsl:value-of select="dueDate"/>
        </td>
    </xsl:template>

    <xsl:template match="invoice" mode="itemList">
        <tr class="heading">
            <td>
                #&#160;&#160;Item
            </td>
                           
            <td>
                Price
            </td>
        </tr>

        <xsl:for-each select="items/item">
            <xsl:choose>
                <xsl:when test="position() = last()">
                    <tr class="item last">
                        <td>
                            <xsl:value-of select="count"/>x
                            <xsl:value-of select="name"/>&#160;
                            (<xsl:value-of select="price"/>,-)
                        </td>
                        <td>
                            <xsl:value-of select="totalPrice"/>,-
                        </td>
                    </tr>
                </xsl:when>
                <xsl:otherwise>
                    <tr class="item">
                        <td>
                            <xsl:value-of select="count"/>x
                            <xsl:value-of select="name"/>&#160;
                            (<xsl:value-of select="price"/>,-)
                        </td>
                        <td>
                            <xsl:value-of select="totalPrice"/>,-
                        </td>
                    </tr>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>

        <tr class="total">
            <td></td>
                
            <td>
                <xsl:value-of select="totalPrice"/>,-
            </td>
        </tr>  
    </xsl:template>

    <xsl:template match="person">
        <xsl:value-of select="name"/>
        <br/>
        <xsl:value-of select="address/streetAddress"/>
        <br/>
        <xsl:value-of select="address/postalCode"/>&#160;
        <xsl:value-of select="address/city"/>
        <br/>
        <xsl:value-of select="address/country"/>
    </xsl:template>
    
    

</xsl:stylesheet>