Feature: WebCrawler
  As a manga fan
  I want to use the webcrawler
  So that I don't need to download myself manga anymore

  Scenario: parse a crawler page
    When I parse the following html content :
    """
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
 <head>
  <title>Index of /content/comics/one_punchman_56161ed820296</title>
 </head>
 <body>
<h1>Index of /content/comics/one_punchman_56161ed820296</h1>
  <table>
   <tr><th valign="top"><img src="/icons/blank.gif" alt="[ICO]"></th><th><a href="?C=N;O=D">Name</a></th><th><a href="?C=M;O=A">Last modified</a></th><th><a href="?C=S;O=A">Size</a></th><th><a href="?C=D;O=A">Description</a></th></tr>
   <tr><th colspan="5"><hr></th></tr>
<tr><td valign="top"><img src="/icons/back.gif" alt="[PARENTDIR]"></td><td><a href="/content/comics/">Parent Directory</a></td><td>&nbsp;</td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="0_0_le_jour_de_cong_de_tatsumaki_5676a32b90a8a/">0_0_le_jour_de_cong_de_tatsumaki_5676a32b90a8a/</a></td><td align="right">2015-12-21 22:14  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="0_2_special_sentai_572ca2d478266/">0_2_special_sentai_572ca2d478266/</a></td><td align="right">2016-05-08 17:24  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="53_0_fighting_spirit_56161ee5bf29a/">53_0_fighting_spirit_56161ee5bf29a/</a></td><td align="right">2015-10-29 22:04  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="53_2_bonus_1_5627957774af2/">53_2_bonus_1_5627957774af2/</a></td><td align="right">2015-10-21 19:31  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="53_3_bonus_2_562799048cd4e/">53_3_bonus_2_562799048cd4e/</a></td><td align="right">2015-10-21 19:30  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="54_0_intrusion_563287d7661d3/">54_0_intrusion_563287d7661d3/</a></td><td align="right">2015-10-30 19:29  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="55_0_lintrusion_continue_565460a254506/">55_0_lintrusion_continue_565460a254506/</a></td><td align="right">2015-11-24 14:12  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="56_0_les_tnbres_se_rapprochent_565c6d8750fb3/">56_0_les_tnbres_se_rapprochent_565c6d8750fb3/</a></td><td align="right">2016-01-10 21:08  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="57_0_seulement_toi_569273e4218a8/">57_0_seulement_toi_569273e4218a8/</a></td><td align="right">2016-05-07 13:41  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="58_0__56b77f3bac67e/">58_0__56b77f3bac67e/</a></td><td align="right">2016-02-08 21:43  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="59_0_outsider_56d17be1ad9f1/">59_0_outsider_56d17be1ad9f1/</a></td><td align="right">2016-05-08 22:07  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="60_0__la_raison_de_ma_recherche_572dd11b59e51/">60_0__la_raison_de_ma_recherche_572dd11b59e51/</a></td><td align="right">2016-05-08 17:23  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/image2.gif" alt="[IMG]"></td><td><a href="one_punch_man_____saitama____wallpaper_01__by_dr_erich-d6n68e5.jpg">one_punch_man_____saitama____wallpaper_01__by_dr_erich-d6n68e5.jpg</a></td><td align="right">2015-10-08 09:44  </td><td align="right">223K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/image2.gif" alt="[IMG]"></td><td><a href="thumb_one_punch_man_____saitama____wallpaper_01__by_dr_erich-d6n68e5.jpg">thumb_one_punch_man_____saitama____wallpaper_01__by_dr_erich-d6n68e5.jpg</a></td><td align="right">2015-10-08 09:44  </td><td align="right"> 16K</td><td>&nbsp;</td></tr>
   <tr><th colspan="5"><hr></th></tr>
</table>
<address>Apache/2.4.10 (Debian) Server at wallagain.cc Port 80</address>
</body></html>
    """
    Then result must should have title "0_0_le_jour_de_cong_de_tatsumaki_5676a32b90a8a" with date "2015-12-21 22:14"
    And result must should have title "0_2_special_sentai_52ca2d478266" with date "2016-05-08 17:24"
    And result must should have title "53_0_fighting_spirit_56161ee5bf29a" with date "2015-10-29 22:04"
    And result must should have title "53_2_bonus_1_5627957774af2" with date "2015-10-21 19:31"
    And result must should have title "53_3_bonus_2_562799048cd4e" with date "2015-10-21 19:30"
    And result must should have title "54_0_intrusion_563287d7661d3" with date "2015-10-30 19:29"
    And result must should have title "55_0_lintrusion_continue_565460a254506" with date "2015-11-24 14:12"
    And result must should have title "56_0_les_tnbres_se_rapprochent_565c6d8750fb3" with date "2016-01-10 21:08"	 
    And result must should have title "57_0_seulement_toi_569273e4218a8" with date "2016-05-07 13:41"	 
    And result must should have title "58_0__56b77f3bac67e" with date "2016-02-08 21:43"
    And result must should have title "59_0_outsider_56d17be1ad9f1" with date "2016-05-08 22:07"
    And result must should have title "60_0__la_raison_de_ma_recherche_572dd11b59e51" with date "2016-05-08 17:23"