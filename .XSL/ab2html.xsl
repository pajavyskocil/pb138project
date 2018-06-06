<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
    
    <xsl:template match="/pdfData">
        <html>
            <head>
                <title>AddressBook PDF</title>
                
                <style type="text/css">
                    .addressbook-box {
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

                    .addressbook-box table {
                        width: 100%;
                        line-height: inherit;
                        text-align: left;
                    }

                    .addressbook-box table td {
                        padding: 5px;
                        vertical-align: top;
                    }

                    .addressbook-box table tr td:nth-child(2) {
                        text-align: right;
                    }

                    .addressbook-box table tr.top table td {
                        padding-bottom: 20px;
                    }

                    .addressbook-box table tr.top table td.title {
                        font-size: 45px;
                        line-height: 45px;
                        color: #333;
                    }

                    .addressbook-box table tr.information table td {
                        padding-bottom: 40px;
                    }

                    .addressbook-box table tr.heading td {
                        background: #eee;
                        border-bottom: 1px solid #ddd;
                        font-weight: bold;
                    }

                    .addressbook-box table tr.person td{
                        border-bottom: 1px solid #eee;
                    }

                    .addressbook-box table tr.person.last td {
                        border-bottom: none;
                    }

                    .addressbook-box table tr.total td:nth-child(2) {
                        border-top: 2px solid #eee;
                        font-weight: bold;
                    }

                    @media only screen and (max-width: 600px) {
                        .addressbook-box table tr.top table td {
                            width: 100%;
                            display: block;
                            text-align: center;
                        }

                        .addressbook-box table tr.information table td {
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
                <div class="addressbook-box">
                    <table cellpadding="0" cellspacing="0">
                        <tr class="top">
                            <td colspan="2">
                                <table>
                                    <tr>
                                        <td class="title">
                                            <img src="CIA-persistence/src/main/resources/LizzardCorpLogo.png" style="width:100%; max-width:300px;"/>
                                        </td>
                                        <td>
                                            AddressBook
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <xsl:apply-templates select="addressBook" mode="personList"/>
                    </table>
                </div>          
            </body>
        </html>
    </xsl:template>

    <xsl:template match="addressBook" mode="head">
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

    <xsl:template match="addressBook" mode="personList">
        <tr class="heading">
            <td>
                Name / Contact
            </td>
                           
            <td>
                Address
            </td>
        </tr>

        <xsl:apply-templates select="person"/>
    </xsl:template>

    <xsl:template match="person">
        <tr class="person">
            <td>
                <xsl:value-of select="name"/>
                <br/>
                <xsl:value-of select="email"/>
                <br/>
                <xsl:value-of select="phone"/>
            </td>
            <td>
                <xsl:apply-templates select="address"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="address">
        <xsl:value-of select="streetAddress"/>
        <br/>
        <xsl:value-of select="postalCode"/>&#160;
        <xsl:value-of select="city"/>
        <br/>
        <xsl:value-of select="country"/>
    </xsl:template>
</xsl:stylesheet>