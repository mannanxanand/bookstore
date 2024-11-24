����   B_
      java/lang/Object <init> ()V  �INSERT INTO books (title, author, category, book_condition, original_price, calculated_price, notes, image_url, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
 
     .com/example/bookstore/utils/DatabaseConnection getConnection ()Ljava/sql/Connection;  java/sql/Statement      java/sql/Connection prepareStatement 1(Ljava/lang/String;I)Ljava/sql/PreparedStatement;
      !com/example/bookstore/models/Book getTitle ()Ljava/lang/String;     ! " java/sql/PreparedStatement 	setString (ILjava/lang/String;)V
  $ %  	getAuthor
  ' (  getCategory
  * +  getCondition
  - . / getOriginalPrice ()D  1 2 3 	setDouble (ID)V
  5 6 / getCalculatedPrice
  8 9  getNotes
  ; <  getImageUrl
  > ? @ getSellerId ()I  B C D setInt (II)V  F G @ executeUpdate  I J K getGeneratedKeys ()Ljava/sql/ResultSet; M N O P Q java/sql/ResultSet next ()Z M S T U getInt (I)I
  W X Y setId (I)V	 [ \ ] ^ _ java/lang/System out Ljava/io/PrintStream;   a b c makeConcatWithConstants &(Ljava/lang/String;)Ljava/lang/String;
 e f g h i java/io/PrintStream println (Ljava/lang/String;)V  k l  close n java/lang/Throwable
 m p q r addSuppressed (Ljava/lang/Throwable;)V  k u java/sql/SQLException
 t w x  printStackTrace z java/util/ArrayList
 y  } SELECT * FROM books   � � createStatement ()Ljava/sql/Statement;  � � � executeQuery ((Ljava/lang/String;)Ljava/sql/ResultSet;
   � id M � T � (Ljava/lang/String;)I � title M � � c 	getString
  � � i setTitle � author
  � � i 	setAuthor � category
  � � i setCategory � book_condition
  � � i setCondition � original_price M � � � 	getDouble (Ljava/lang/String;)D
  � � � setOriginalPrice (D)V � calculated_price
  � � � setCalculatedPrice � notes
  � � i setNotes � 	image_url
  � � i setImageUrl � 	seller_id
  � � Y setSellerId � � � � � java/util/List add (Ljava/lang/Object;)Z M k  k � java/lang/StringBuilder � SELECT * FROM books WHERE 1=1
 � �  i
 � � � � Q java/lang/String isEmpty � $ AND (title LIKE ? OR author LIKE ?)
 � � � � append -(Ljava/lang/String;)Ljava/lang/StringBuilder; � � �  AND ( � � � @ size � category LIKE ? �  OR  � ) �  AND book_condition IN ( � ? � ,  �  AND calculated_price <= ?
 � � �  toString  �  � 0(Ljava/lang/String;)Ljava/sql/PreparedStatement;  a � � � � iterator ()Ljava/util/Iterator; � � � � Q java/util/Iterator hasNext � � P  ()Ljava/lang/Object;  � K  SELECT * FROM books WHERE id = ? �UPDATE books SET title = ?, author = ?, category = ?, book_condition = ?, original_price = ?, calculated_price = ?, notes = ?, image_url = ? WHERE id = ?
 	 @ getId  a DELETE FROM books WHERE id = ?  b (I)Ljava/lang/String; 'SELECT * FROM books WHERE seller_id = ? !com/example/bookstore/dao/BookDAO Code LineNumberTable LocalVariableTable this #Lcom/example/bookstore/dao/BookDAO; addBook &(Lcom/example/bookstore/models/Book;)V rs Ljava/sql/ResultSet; pstmt Ljava/sql/PreparedStatement; conn Ljava/sql/Connection; e Ljava/sql/SQLException; book #Lcom/example/bookstore/models/Book; query Ljava/lang/String; StackMapTable getAllBooks ()Ljava/util/List; stmt Ljava/sql/Statement; books Ljava/util/List; LocalVariableTypeTable 5Ljava/util/List<Lcom/example/bookstore/models/Book;>; 	Signature 7()Ljava/util/List<Lcom/example/bookstore/models/Book;>; searchBooks E(Ljava/lang/String;Ljava/util/List;Ljava/util/List;D)Ljava/util/List; i I 	condition 
paramIndex 
searchText 
categories 
conditions maxPrice D Ljava/lang/StringBuilder; $Ljava/util/List<Ljava/lang/String;>; �(Ljava/lang/String;Ljava/util/List<Ljava/lang/String;>;Ljava/util/List<Ljava/lang/String;>;D)Ljava/util/List<Lcom/example/bookstore/models/Book;>; getBookById &(I)Lcom/example/bookstore/models/Book; 
updateBook 
deleteBook getBooksBySellerId (I)Ljava/util/List; sellerId 8(I)Ljava/util/List<Lcom/example/bookstore/models/Book;>; 
SourceFile BookDAO.java BootstrapMethodsL Book added: N %%P Book updated: R Book deleted with ID: T
UVW bX $java/lang/invoke/StringConcatFactory �(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; InnerClasses[ %java/lang/invoke/MethodHandles$Lookup] java/lang/invoke/MethodHandles Lookup !             /     *� �          
             �    M� 	N-,�  :+� �  +� #�  +� &�  +� )�  +� ,� 0 +� 4� 0 +� 7�  +� :�  	+� =� A � E W� H :� L � +� R � V� Z+� � `  � d� *� j �  :� � j � :� o�-� '-� s � :-� -� s � :� o�� N-� v�   � � m � � � m  � � m � � � m  t    f             )  5  A  M  Z  g  t  �  �  �  �  � # � % �  � % �  ' % & (   H  � %   �     !"       #$  %& '   z � �   �   M  �    �    m�    �   m  m� L m�    �  m  m� B t ()       V� yY� {L|M� 	N-� ~ :,� � :� L � �� Y� �:�� � � V�� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � �+� � W��X� *� � �  :� � � � :� o�� *� � �  :� � � � :� o�-� '-� s � :-� -� s � :� o�� N-� v+�  ! � � m � � � m  � m m $1 m7=@ m LO t    v    +  ,  -  .  / ! 1 + 2 4 4 B 5 P 6 ^ 7 l 8 z 9 � : � ; � < � = � ? � @ � A � - � A -$ A1 -L CO AP BT D   R  4 �#$  ! �  *+  =  P !"   V   N,-  K%& .     N,/ '   � � !  � �   M  � �N m�   � �   M m  m� N m�   � �   m  m� L m�   � �  m  m� B t0   1 23   s    û yY� {:� �Y˷ �:+� +� Ϛ Զ �W,� M,� � � D۶ �W6,� � � &� �W,� � d� � �W����� �W-� M-� � � D� �W6-� � � &� �W-� � d� � �W����� �W�� � �W� 	:� � � :	6
+� .+� Ϛ '	
�
+� �  �  	
�
+� �  �  ,� @,� � � 7,� � :� � � %� � � �:	
�
� �  �  ���-� ;-� � � 2-� � :� � �  � � � �:	
�
�  ����� 	
�
� 0 	� :� L � �� Y� �:�� � � V�� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � �� � W��W	� *	� j �  :
	� 	� j � :
� o
�� *� s �  :	� � s � :
	
� o	�� 
:� v�  �^m mt{~ m ��� m��� m ��� t    � =   I 	 J  L  M ' P 4 Q < R J S R T _ U g R m X u [ � \ � ] � ^ � _ � ` � ] � c � f � g � j � k � m � o � p q t$ uB vU wX ze {� |� }� �� �� �� �� �� �� �� �� �� � � �' �5 �C �Q �[ �^ �m j� �� j� �� �� �� �   �  ? .45  � .45 B  �& � 6& � �#$  �v75 
� �  �� 	 ��  � !"   �    �8&   �9-   �:-   �;<  	�,-  �%= .       �9>   �:>  	�,/ '   � � ' � �� '� � '� � D  �  �� +�  �� &�  M� �N m�  
 � � � � �   m  m� N m�  	 � � � � �  m  m� B t0   ? @A   �    =M� 	N-,� � :� A � :� L � �� Y� �:�� � � V�� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � �:� 
� j -� 	-� s �� *� j �  :� � j � :� o�-� '-� s � :-� -� s � :� o�� N-� v�   � � m � � � m  � m � m$' m  �6 t �36 t    r    �  �  �  �  � # � - � 6 � D � R � ` � n � | � � � � � � � � � � � � � � � � � � � � �3 �6 �7 �; �   R  6 �#$  # �   �  +  7 !"   =    = �5  9%& '   i � �  �   M    	� N m�   �   m  m� L m�   �  m  m� B t B   4     �M� 	N-,� � :+� �  +� #�  +� &�  +� )�  +� ,� 0 +� 4� 0 +� 7�  +� :�  	+�� A � E W� Z+� �
  � d� *� j �  :� � j � :� o�-� '-� s � :-� -� s � :� o�� N-� v�   � � m � � � m  � � m � � � m  � � t    Z    �  �  �  �  � ) � 5 � A � M � Z � g � t � � � � � � � � � � � � � � � � � � � � �   >   �   �   � !"    �     �#$   �%& '   a 
� �   �    m�    �   m  m� L m�    �  m  m� B t C Y   �     �M� 	N-,� � :� A � E W� Z�  � d� *� j �  :� � j � :� o�-� '-� s � :-� -� s � :� o�� N-� v�   . = m D K N m  Z g m m s v m  � � t    :    �  �  �  �  � " � . � = � Z � g � � � � � � � � �   >   I   z   � !"    �     � �5   �%& '   [ 
� =  �    m�   �   m  m� L m�   �  m  m� B t DE   �    <� yY� {MN� 	:-� � :� A � :� L � �� Y� �:�� � � V�� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � ��� � � �,� � W��X� *� j �  :� � j � :� o�� *� s �  :� � s � :� o�� 
:� v,�   � � m � � � m  m!$ m 03 t    r    �  �  �  �  � $ � - � 7 � @ � N � \ � j � x � � � � � � � � � � � � � � � � � � � � �0 �3 �5 �:    \ 	 @ �#$  - �   �    5 !"   <    <F5  4,-  0%& .     4,/ '   k � -  � �   M  � �N m�   � �   m  m� N m�   � �  m  m� B t0   G H   IJ    S KS MS OS QY   
 Z\^ 