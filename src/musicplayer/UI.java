/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import net.proteanit.sql.DbUtils;
import java.sql.*;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author gene
 */
public class UI extends javax.swing.JFrame {

    /**
     * Creates new form MusicplayerUI
     */
    ActionListener actionListener;
    Timer t1;
    DBEngine dbengine;
    VLCEngine me;
    Random r;                           //r..randomnumbergenerator used for shuffle
    int toBePoppedNext;
    int randnext;
    int next;
    Stack<Integer> historyStack;        //Used for Previous
    int nowPlayingID = 1;
    int currentSongDuration;
    int elapsed;

    private void LibraryUpdate() {
        try {
            libraryTable.setModel(DbUtils.resultSetToTableModel(dbengine.rsLibraryRead));
            TableColumnModel tc = libraryTable.getColumnModel();
            TableColumn k = tc.getColumn(0);
            k.setMaxWidth(50);
            k.setMinWidth(50);
            TableColumn k1 = tc.getColumn(1);
            k1.setMinWidth(300);
            k1.setMaxWidth(300);
            TableColumn k4 = tc.getColumn(5);
            k4.setMaxWidth(150);

        } catch (Exception e) {
        }
    }

    private void UpdateQueue() {
        try {
            dbengine.readQueue();
            queueTable.setModel(DbUtils.resultSetToTableModel(dbengine.rsQueueRead));
            queueTable.setRowHeight(50);
            TableColumnModel tc = queueTable.getColumnModel();
            TableColumn k = tc.getColumn(0);
            k.setMaxWidth(50);

        } catch (Exception e) {
        }
    }

    public UI() throws SQLException, InterruptedException {
        this.dbengine = new DBEngine();
        initComponents();
        dbengine = new DBEngine();
        me = new VLCEngine();
        historyStack = new Stack<>();
        dbengine.readLibrary();
        dbengine.readQueue();
        UpdateQueue();
        LibraryUpdate();
        me.load(dbengine.getFile(Integer.parseInt(libraryTable.getValueAt(0, 0).toString())));
        nowPlayingID = 1;
        r = new Random();
        initMediaControls();

        next = 0;
        toBePoppedNext = -1;
        randnext = 0;
    }

    void musicChangeOver(int trackid) throws SQLException, InterruptedException {
        try {
            nowPlayingID = trackid;
            me.load(dbengine.getFile(trackid));
            historyStack.push(trackid);
            initMediaControls();
            me.play();
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }

        trackNameLabel.setText(dbengine.getTitle(trackid));
        if (!dbengine.getArtist(trackid).equals("")) {
            artistalbumLabel.setText(dbengine.getAlbum(trackid) + " by " + dbengine.getArtist(trackid));
        } else {
            artistalbumLabel.setText(dbengine.getAlbum(trackid));
        }
        t1.start();
    }

    void playFinallybyCheckingShuffleRepeat() throws SQLException, InterruptedException {
        if (shuffleButton.isSelected() && queueTable.getRowCount() > 1) {

            randnext = r.nextInt(queueTable.getRowCount() - 1);

            if (!repeatButton.isSelected() && toBePoppedNext != -1) {
                dbengine.popQueue(Integer.parseInt(queueTable.getValueAt(toBePoppedNext, 0).toString()));
                UpdateQueue();
            }
            musicChangeOver(Integer.parseInt(queueTable.getValueAt(randnext, 0).toString()));


            toBePoppedNext = randnext;
        } else {
            if (!repeatButton.isSelected() && toBePoppedNext != -1) {
                dbengine.popQueue(Integer.parseInt(queueTable.getValueAt(toBePoppedNext, 0).toString()));
                UpdateQueue();
            }
            musicChangeOver(Integer.parseInt(queueTable.getValueAt(0, 0).toString()));
            toBePoppedNext = 0;

        }
    }

    private void initMediaControls() throws SQLException {


        //currentSongDuration = me.getMusicDuration();
        currentSongDuration = dbengine.getLength(nowPlayingID);
        seekBar.setMaximum(currentSongDuration);
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String label = "";
                if (elapsed / 60 < 10) {
                    label += "0";
                }
                label += (elapsed / 60);
                label += ":";
                if (elapsed % 60 < 10) {
                    label += "0";
                }
                label += elapsed % 60;

                timeElapsedLabel.setText(label);
                seekBar.setValue(elapsed);
                elapsed = me.getElapsedTime();
                if (elapsed > currentSongDuration - 2) {
                    try {
                        seekBar.setValue(0);
                        timeElapsedLabel.setText("00:00");
                        playButton.setLabel("Pause");
                        playFinallybyCheckingShuffleRepeat();
                        me.play();
                    } catch (            SQLException | InterruptedException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        try {
                            if (!shuffleButton.isSelected()) {
                                musicChangeOver(Integer.parseInt(libraryTable.getValueAt(next++, 0).toString()));
                            } else {
                                next = r.nextInt(libraryTable.getRowCount() - 1);
                                musicChangeOver(Integer.parseInt(libraryTable.getValueAt(next, 0).toString()));
                            }
                        } catch (SQLException | InterruptedException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
                if (me.State()) {
                    playButton.setLabel("Play");
                } else {
                    playButton.setLabel("Pause");
                }
                if (queueTable.getRowCount() == 0) {
                    toBePoppedNext = -1;
                }


            }
        };
        t1 = new Timer(200, actionListener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        aboutDialog = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        oslDialog = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        libraryPopupMenu = new javax.swing.JPopupMenu();
        playPopupMenuItem = new javax.swing.JMenuItem();
        enqueuePopupMenuItem = new javax.swing.JMenuItem();
        queuePopupMenu = new javax.swing.JPopupMenu();
        playQueuePopupMenuItem = new javax.swing.JMenuItem();
        removeQueuePopupMenuItem = new javax.swing.JMenuItem();
        fileChooserDialog = new javax.swing.JDialog();
        fileChooser = new javax.swing.JFileChooser();
        nextButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        shuffleButton = new javax.swing.JToggleButton();
        repeatButton = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        libraryTable = new javax.swing.JTable();
        trackNameLabel = new javax.swing.JLabel();
        artistalbumLabel = new javax.swing.JLabel();
        timeElapsedLabel = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        seekBar = new javax.swing.JProgressBar();
        jScrollPane4 = new javax.swing.JScrollPane();
        queueTable = new javax.swing.JTable();
        albumartLabel = new javax.swing.JLabel();
        previousButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        oslMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        aboutMenuItem = new javax.swing.JMenuItem();

        aboutDialog.setTitle("About");
        aboutDialog.setAlwaysOnTop(true);
        aboutDialog.setMinimumSize(new java.awt.Dimension(298, 126));

        jLabel6.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Java Music Player");

        jLabel7.setText("Created by Asish,Ashwin,Atul");

        jLabel8.setFont(new java.awt.Font("Cantarell", 2, 13)); // NOI18N
        jLabel8.setText("vere level");

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(54, 54, 54))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        oslDialog.setTitle("Open Source Licenses");
        oslDialog.setMinimumSize(new java.awt.Dimension(400, 300));
        oslDialog.setModal(true);

        jTextPane1.setContentType("text/html"); // NOI18N
        jTextPane1.setText(" <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\"> <HTML> <HEAD>   <!--#config timefmt=\"%d-%b-%y\" -->   <TITLE>JMF - Attributions</TITLE>   <META NAME=\"keywords\" CONTENT=\"JMF, Attributions, Java Media Framework\"> </HEAD>  <BODY BGCOLOR=\"#FFFFFF\" TEXT=\"#000000\" LINK=\"8B4513\" VLINK=\"8B4513\" >  <!-- page headline --> <FONT SIZE=\"+2\"> JMF Attributions </FONT> <BR>  <!-- end page headline -->  <!-- optional subnav links --> <!-- end optional subnav links -->  <!-- main page content -->  <p> The Java<SUP><FONT SIZE=\"-2\">TM</FONT></SUP> Media Framework reference implementation was developed by by Sun Microsystems, Inc. It is based in part on software written by external individuals and organizations, who are listed below along with the copyrights and conditions by which their technology is allowed to be used in JMF. </p>  <!---------------------------------------------------------------> <hr>  <b> <p> Java Versions of: GSM Decoder, GSM Encoder, MPEG-1 Video Decoder,  ADPCM Encoder, ADPCM Decoder, A-law Decoder and H.263 Decoder  <p> Native Versions of: MPEG-1 Parser, H.263 Encoder, GSM Encoder </b>  <p> Copyright &copy; IBM Corporation 1997-1999 All Rights Reserved  <p><FONT SIZE=-1> Licensed Materials - Property of IBM<br> \"Restricted Materials of IBM\" 5748-B81<br> US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corporation.</font> </p>  <!---------------------------------------------------------------> <hr>  <p> <b> GSM 06.10</b>  <p> Copyright &copy; 1992, 1993, 1994 by Jutta Degener and Carsten Bormann, Technische Universitaet Berlin  <P><FONT SIZE=-1>Any use of this software is permitted provided that this notice is not removed and that neither the authors nor the Technische Universitaet Berlin are deemed to have made any representations as to the suitability of this software for any purpose nor are held responsible for any defects of this software.&nbsp; THERE IS ABSOLUTELY NO WARRANTY FOR THIS SOFTWARE.</FONT>  <P><FONT SIZE=-1>As a matter of courtesy, the authors request to be informed about uses this software has found, about bugs in this software, and about any improvements that may be of general interest.</FONT> </p>  <!---------------------------------------------------------------> <hr>  <p> <b> DVI ADPCM</b>  <p> Copyright &copy; 1992 by Stichting Mathematisch Centrum, Amsterdam, The Netherlands. All rights reserved.  <P><FONT SIZE=-1>Permission to use, copy, modify, and distribute this software and its documentation for any purpose and without fee is hereby granted, provided that the above copyright notice appear in all copies and that both that copyright notice and this permission notice appear in supporting documentation, and that the names of Stichting Mathematisch Centrum or CWI not be used in advertising or publicity pertaining to distribution of the software without specific, written prior permission.</FONT>  <P><FONT SIZE=-1>STICHTING MATHEMATISCH CENTRUM DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO EVENT SHALL STICHTING MATHEMATISCH CENTRUM BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.</FONT> </p>  <!---------------------------------------------------------------> <hr>  <p> <b> H.261, Motion JPEG</b>  <p> This product includes software developed by the University of California, Berkeley and the Network Research Group at Lawrence Berkeley Laboratory.  <P>Copyright &copy; 1993-1994 The Regents of the University of California. All rights reserved.  <P><FONT SIZE=-1> Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following  conditions are met:  <ol> <!-- 1 --> <li>  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.  <!-- 2 --> <li>  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.  <!-- 3 --> <li>  All advertising materials mentioning features or use of this software must display the following acknowledgement: This product includes software developed by the University of California, Berkeley and the Network Research Group at Lawrence Berkeley Laboratory.  <!-- 4 --> <li>  Neither the name of the University nor of the Laboratory may be used to endorse or promote products derived from this software without specific prior written permission. </ol>  <P> THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.&nbsp; IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. </p> </font>  <!---------------------------------------------------------------> <hr>  <p> <b> JPEG</b>  <p> This software is based in part on the work of the  <a href=\"http://www.ijg.org\">Independent JPEG Group</a>. The following is an exerpt from the README file which comes with their  software: </p>  <font size=\"-1\">  <b><underline> LEGAL ISSUES </underline></b>  <p> In plain English:  <ol>  <!-- 1. --> <li> We don't promise that this software works.   (But if you find any bugs, please let us know!)   <!-- 2. --> <li> You can use this software for whatever you want.   You don't have to pay us.  <!-- 3. --> <li> You may not pretend that you wrote this software.  If you use it in a program, you must acknowledge somewhere in your documentation that you've used the IJG code. </ol>  <p> In legalese:  <p> The authors make NO WARRANTY or representation, either express or implied, with respect to this software, its quality, accuracy, merchantability, or fitness for a particular purpose.  This software is provided \"AS IS\", and you, its user, assume the entire risk as to its quality and accuracy.  <p> This software is copyright (C) 1991-1998, Thomas G. Lane. All Rights Reserved except as specified below.  <p> Permission is hereby granted to use, copy, modify, and distribute this software (or portions thereof) for any purpose, without fee, subject to these conditions:  <ol> <!-- (1) --> <li> If any part of the source code for this software is distributed, then this README file must be included, with this copyright and no-warranty notice unaltered; and any additions, deletions, or changes to the original files must be clearly indicated in accompanying documentation.  <!-- (2) --> <li> If only executable code is distributed, then the accompanying documentation must state that \"this software is based in part on the work of the Independent JPEG Group\".  <!-- (3) --> <li> Permission for use of this software is granted only if the user accepts full responsibility for any undesirable consequences; the authors accept NO LIABILITY for damages of any kind. </ol>  <p> These conditions apply to any software derived from or based on the IJG code, not just to the unmodified library.  If you use our work, you ought to acknowledge us.  <p> Permission is NOT granted for the use of any IJG author's name or company name in advertising or publicity relating to this software or products derived from it.  This software may be referred to only as \"the Independent JPEG Group's software\".  <p> We specifically permit and encourage the use of this software as the basis of commercial products, provided that all warranty or liability claims are assumed by the product vendor. </p> </font>  <!---------------------------------------------------------------> <hr>  <p> <b> VIVO, H.263</b>  <p> Vivo is a product of the Vivo Software, Inc. at Waltham, MA 02154.8414. <p> Copyright &copy; 1995 Vivo Software, Inc. All rights reserved. </p>  <!---------------------------------------------------------------> <hr>  <h2>Trademarks</h2>  <P>Java<SUP><FONT SIZE=-2>TM</FONT></SUP> is a trademark of Sun Microsystems, Inc. <BR>QuickTime<SUP><FONT SIZE=-2>TM</FONT></SUP> is a trademark of Apple Computer, Inc. <BR>AVI<SUP><FONT SIZE=-2>TM</FONT></SUP> is a trademark of Microsoft Corporation. <BR>Vivo<SUP><FONT SIZE=-2>TM</FONT></SUP> is a trademark of Vivo Software, Inc. <!--Blank Space--> </P>   <!-- end main page content -->  </BODY> </HTML>  ");
        jTextPane1.setToolTipText("");
        jScrollPane3.setViewportView(jTextPane1);

        javax.swing.GroupLayout oslDialogLayout = new javax.swing.GroupLayout(oslDialog.getContentPane());
        oslDialog.getContentPane().setLayout(oslDialogLayout);
        oslDialogLayout.setHorizontalGroup(
            oslDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oslDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        oslDialogLayout.setVerticalGroup(
            oslDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, oslDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        playPopupMenuItem.setText("Play");
        playPopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPopupMenuItemActionPerformed(evt);
            }
        });
        libraryPopupMenu.add(playPopupMenuItem);

        enqueuePopupMenuItem.setText("Enqueue");
        enqueuePopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enqueuePopupMenuItemActionPerformed(evt);
            }
        });
        libraryPopupMenu.add(enqueuePopupMenuItem);

        playQueuePopupMenuItem.setText("Play");
        playQueuePopupMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                playQueuePopupMenuItemMouseReleased(evt);
            }
        });
        queuePopupMenu.add(playQueuePopupMenuItem);

        removeQueuePopupMenuItem.setText("Remove");
        removeQueuePopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeQueuePopupMenuItemActionPerformed(evt);
            }
        });
        queuePopupMenu.add(removeQueuePopupMenuItem);

        fileChooserDialog.setMinimumSize(new java.awt.Dimension(440, 430));

        fileChooser.setMinimumSize(new java.awt.Dimension(435, 439));
        fileChooser.setPreferredSize(new java.awt.Dimension(435, 430));
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fileChooserDialogLayout = new javax.swing.GroupLayout(fileChooserDialog.getContentPane());
        fileChooserDialog.getContentPane().setLayout(fileChooserDialogLayout);
        fileChooserDialogLayout.setHorizontalGroup(
            fileChooserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileChooserDialogLayout.createSequentialGroup()
                .addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 183, Short.MAX_VALUE))
        );
        fileChooserDialogLayout.setVerticalGroup(
            fileChooserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileChooserDialogLayout.createSequentialGroup()
                .addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 101, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Music Player");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        shuffleButton.setText("Shuffle");

        repeatButton.setText("Repeat");
        repeatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Queue");

        jLabel2.setText("Library");

        libraryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Track", "Title", "Artist", "Album", "Duration"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        libraryTable.setToolTipText("<html><head><b>MusicLibrary<br></b>This is your entire music collection</head></html>");

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${componentPopupMenu}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, libraryPopupMenu, eLProperty, libraryTable, "");
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        libraryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                libraryTableMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                libraryTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(libraryTable);
        libraryTable.getColumnModel().getColumn(0).setResizable(false);
        libraryTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        libraryTable.getColumnModel().getColumn(1).setResizable(false);
        libraryTable.getColumnModel().getColumn(2).setResizable(false);

        trackNameLabel.setFont(new java.awt.Font("Cantarell", 1, 17)); // NOI18N

        artistalbumLabel.setFont(new java.awt.Font("Cantarell", 2, 11)); // NOI18N

        timeElapsedLabel.setText("00:00");

        searchTextField.setFont(new java.awt.Font("Cantarell", 2, 13)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(29, 25, 27));
        searchTextField.setText("Search all fields");
        searchTextField.setToolTipText("Search all fields(Track,Album,Artist)");
        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });
        searchTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchTextFieldFocusLost(evt);
            }
        });
        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });

        seekBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        seekBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seekBarMouseClicked(evt);
            }
        });
        seekBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                seekBarMouseDragged(evt);
            }
        });

        queueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title"
            }
        ));
        queueTable.setToolTipText("<html><head><b>Queue<br></b>Add songs here for continious playback</head></html>");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, queuePopupMenu, org.jdesktop.beansbinding.ObjectProperty.create(), queueTable, org.jdesktop.beansbinding.BeanProperty.create("elements"));
        bindingGroup.addBinding(binding);
        binding.bind();
        queueTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                queueTableMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(queueTable);

        albumartLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/musicplayer/albumart_mp_unknown_list.png"))); // NOI18N

        previousButton.setText("Previous");
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setText("Scan Media");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(openMenuItem);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("Quit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu4.setText("Control");
        jMenuBar1.add(jMenu4);

        jMenu3.setText("Help");

        oslMenuItem.setText("Open Source Licenses");
        oslMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oslMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(oslMenuItem);
        jMenu3.add(jSeparator2);

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(aboutMenuItem);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(albumartLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(204, 204, 204)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(previousButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)
                        .addGap(18, 18, 18)
                        .addComponent(shuffleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repeatButton)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(artistalbumLabel)
                            .addComponent(trackNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(seekBar, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeElapsedLabel)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(nextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(playButton)
                        .addComponent(shuffleButton)
                        .addComponent(repeatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(previousButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(trackNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(artistalbumLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(seekBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeElapsedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 27, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(albumartLabel))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed

        try {
            playFinallybyCheckingShuffleRepeat();
        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                if (!shuffleButton.isSelected()) {
                    if(next==libraryTable.getRowCount()-1)
                        next=0;
                    else
                        next++;
                    musicChangeOver(Integer.parseInt(libraryTable.getValueAt(next, 0).toString()));
                } else {
                    next = r.nextInt(libraryTable.getRowCount() - 1);
                    musicChangeOver(Integer.parseInt(libraryTable.getValueAt(next, 0).toString()));
                }
            } catch (SQLException | InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_nextButtonActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void oslMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oslMenuItemActionPerformed
        oslDialog.setVisible(true);
    }//GEN-LAST:event_oslMenuItemActionPerformed

    private void seekBarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_seekBarMouseClicked
        boolean initState = me.State();
        if (initState) {
            playButton.setLabel("Play");
        } else {
            playButton.setLabel("Pause");
        }
        double val = 100 * evt.getX() / seekBar.getWidth();
        // seekBar.setValue(((int) (me.getMusicDuration() * val) / 100));
        me.seek((int) ((currentSongDuration * val) / 100));
        seekBar.setValue(((int) (currentSongDuration * val) / 100));
        elapsed = (int) ((currentSongDuration * val) / 100);

    }//GEN-LAST:event_seekBarMouseClicked

    private void seekBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_seekBarMouseDragged
        boolean initState = me.State();
        if (initState) {
            playButton.setLabel("Play");
        } else {
            playButton.setLabel("Pause");
        }
        double val = 100 * evt.getX() / seekBar.getWidth();
        me.seek((int) ((currentSongDuration * val) / 100));
        seekBar.setValue(((int) (currentSongDuration * val) / 100));
        elapsed = (int) ((currentSongDuration * val) / 100);
    }//GEN-LAST:event_seekBarMouseDragged

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed

        if (seekBar.getValue() == 0) {
            try {
                if (shuffleButton.isSelected() && queueTable.getRowCount() > 1) {
                    int t;

                    t = r.nextInt(queueTable.getRowCount() - 1);
                    musicChangeOver(Integer.parseInt(queueTable.getValueAt(t, 0).toString()));
                    toBePoppedNext = t;
                } else {
                    musicChangeOver(Integer.parseInt(queueTable.getValueAt(0, 0).toString()));
                    toBePoppedNext = 0;
                }

            } catch (SQLException | InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                try {

                    musicChangeOver(Integer.parseInt(libraryTable.getValueAt(0, 0).toString()));//what does the fox say? change this ti the first playable song!
                } catch (SQLException | InterruptedException ex) {
                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (playButton.getLabel().equals("Play")) {

            playButton.setLabel("Pause");
            try {
                me.play();
            } catch (InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            playButton.setLabel("Play");
            try {
                me.pause();
            } catch (InterruptedException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            t1.stop();
        }
        t1.start();
    }//GEN-LAST:event_playButtonActionPerformed

    private void searchTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTextFieldFocusGained

        if (searchTextField.getText().equals("Search all fields")) {
            searchTextField.setText("");
        }

    }//GEN-LAST:event_searchTextFieldFocusGained

    private void searchTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTextFieldFocusLost

        if ("".equals(searchTextField.getText())) {
            searchTextField.setText("Search all fields");
        }
        try {
            libraryTable.setModel(DbUtils.resultSetToTableModel(dbengine.rsLibrarySearch));
            LibraryUpdate();
        } catch (NullPointerException e) {
            System.out.println("zeroSearchResults!");
        }
    }//GEN-LAST:event_searchTextFieldFocusLost

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
    }//GEN-LAST:event_searchTextFieldActionPerformed

    
    private void repeatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatButtonActionPerformed

    }//GEN-LAST:event_repeatButtonActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        fileChooserDialog.show();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        LibraryUpdate();
    }//GEN-LAST:event_formWindowOpened

    private void libraryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_libraryTableMouseClicked

    }//GEN-LAST:event_libraryTableMouseClicked

    private void playPopupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPopupMenuItemActionPerformed
        int[] rows = libraryTable.getSelectedRows();
        String id = libraryTable.getValueAt(rows[0], 0).toString();
        try {
            next=rows[0]+1;
            if(next==libraryTable.getRowCount()) {
            
                next=0;
            }
            musicChangeOver(Integer.parseInt(id));
        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_playPopupMenuItemActionPerformed

    private void libraryTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_libraryTableMouseReleased

        if (evt.isPopupTrigger()) {
            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int col = source.columnAtPoint(evt.getPoint());
            System.out.println(row);
            if (!source.isRowSelected(row)) {
                source.changeSelection(row, col, false, false);
            }
        }
        if (evt.getButton() == 3) {
            libraryPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }

    }//GEN-LAST:event_libraryTableMouseReleased

    private void searchTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyTyped
    }//GEN-LAST:event_searchTextFieldKeyTyped

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        System.out.println(searchTextField.getText());
        try {
            dbengine.searchLibrary(searchTextField.getText());
            libraryTable.setModel(DbUtils.resultSetToTableModel(dbengine.rsLibrarySearch));
            TableColumnModel tc = libraryTable.getColumnModel();
            TableColumn k = tc.getColumn(0);
            k.setMaxWidth(50);
            k.setMinWidth(50);
            TableColumn k1 = tc.getColumn(1);
            k1.setMinWidth(300);
            k1.setMaxWidth(300);
            TableColumn k4 = tc.getColumn(4);
            k4.setMaxWidth(300);

        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void enqueuePopupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enqueuePopupMenuItemActionPerformed
        int[] rows = libraryTable.getSelectedRows();
        for (int row : rows) {
            String id = libraryTable.getValueAt(row, 0).toString();
            String title = libraryTable.getValueAt(row, 1).toString();
            String album = libraryTable.getValueAt(row, 2).toString();
            String artist = libraryTable.getValueAt(row, 3).toString();

            try {
                dbengine.writeQueue(Integer.parseInt(id), title, album, artist);
                UpdateQueue();
            } catch (SQLException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_enqueuePopupMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            dbengine.clearQueue();
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void queueTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_queueTableMouseReleased

        if (evt.isPopupTrigger()) {
            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int col = source.columnAtPoint(evt.getPoint());
            System.out.println(row);
            if (!source.isRowSelected(row)) {
                source.changeSelection(row, col, false, false);
            }
        }
        if (evt.getButton() == 3) {
            queuePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_queueTableMouseReleased

    private void playQueuePopupMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playQueuePopupMenuItemMouseReleased

        int[] rows = queueTable.getSelectedRows();
        String id = queueTable.getValueAt(rows[0], 0).toString();
        try {
            musicChangeOver(Integer.parseInt(id));

        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_playQueuePopupMenuItemMouseReleased

    private void removeQueuePopupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeQueuePopupMenuItemActionPerformed

        int[] rows = queueTable.getSelectedRows();
        for (int row : rows) {
            String id = queueTable.getValueAt(row, 0).toString();

            try {
                dbengine.popQueue(Integer.parseInt(id));


            } catch (SQLException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        UpdateQueue();
    }//GEN-LAST:event_removeQueuePopupMenuItemActionPerformed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
    
    }//GEN-LAST:event_formKeyReleased

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        try {
            historyStack.pop();
            int trackid = historyStack.pop();

            musicChangeOver(trackid);
        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyStackException e) {
        }

    }//GEN-LAST:event_previousButtonActionPerformed

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed
        System.out.println(fileChooser.getSelectedFile().getAbsolutePath());

    }//GEN-LAST:event_fileChooserActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void run() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            @SuppressWarnings("empty-statement")
            public void run() {
                try {
                    new UI().setVisible(true);
                } catch (SQLException | InterruptedException ex) {
                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel albumartLabel;
    private javax.swing.JLabel artistalbumLabel;
    private javax.swing.JMenuItem enqueuePopupMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JDialog fileChooserDialog;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JPopupMenu libraryPopupMenu;
    private javax.swing.JTable libraryTable;
    private javax.swing.JButton nextButton;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JDialog oslDialog;
    private javax.swing.JMenuItem oslMenuItem;
    private javax.swing.JButton playButton;
    private javax.swing.JMenuItem playPopupMenuItem;
    private javax.swing.JMenuItem playQueuePopupMenuItem;
    private javax.swing.JButton previousButton;
    private javax.swing.JPopupMenu queuePopupMenu;
    private javax.swing.JTable queueTable;
    private javax.swing.JMenuItem removeQueuePopupMenuItem;
    private javax.swing.JToggleButton repeatButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JProgressBar seekBar;
    private javax.swing.JToggleButton shuffleButton;
    private javax.swing.JLabel timeElapsedLabel;
    private javax.swing.JLabel trackNameLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
