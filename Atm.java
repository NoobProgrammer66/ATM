import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.lang.String;
import java.time.LocalDateTime;

public class Atm {
    public static int user_num;
    public static String user_name[] = new String[1000];
    public static int password[] = new int[1000];
    public static long card_Id[] = new long[1000];
    public static long money[] = new long[1000];
    public static String[][] transfer_Record = new String[1000][1000];
    public static int flag;
    public static int tr[]=new int[1000];
    public static int pass ;
    public static long id_check;
    public static int mark =-1;
    public static LocalDateTime time;

    public static void main(String args[]) {
        Record();
        if (mark==-1){
            ReadFile();
        }
        while (true) {
            Record();
            mainMenu();
        }
    }
    static void Register() {
        flag=user_num;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your name : ");
        user_name[user_num] = input.nextLine();
        System.out.println("Enter your card id : ");
        id_check = input.nextLong();
        Id_Duplicate();
        System.out.println("The password must be contain 6 number\nEnter your password :");
        pass =input.nextInt();
        PassStrong();
        System.out.println("Enter how much money do you want to deposit :");
        money[user_num] = input.nextLong();
        System.out.println("Register successful :");
        user_num++;
        WriteToFile();
    }
    static void Login() {
        int flag2=-1;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your card id : ");
        long id = input.nextLong();
        System.out.println("Enter your password :");
        int passcode = input.nextInt();
        if(card_Id[0]== id && password[0]==passcode){
            Admin();
            flag2=1;
        }
        for (int i = 1; i < user_num; i++) {
            if (card_Id[i] == id && password[i] == passcode) {
                flag = i;
                int aa=0;
                int bb=0;
                flag2=1;
                boolean condition = true;
                while (condition) {
                    int flag1 = -1;
                    aa=tr[flag];
                    System.out.println("User Name :" + user_name[flag]);
                    System.out.println("Amount of money : " + money[flag] + " MMK");
                    System.out.println("Enter 1 to deposit money : \nEnter 2 to withdraw money :\nEnter 3 to transfer money :\nEnter 4 to change password :\nEnter 5 to view transfer record :\nEnter 6 to exit :");
                    int choose = input.nextInt();
                    switch (choose) {
                        case 1:
                            System.out.println("Enter how much money do you want to deposit :");
                            long deposit = input.nextLong();
                            money[flag] += deposit;
                            break;
                        case 2:
                            System.out.println("Enter how much money do you want to withdraw :");
                            long withdraw = input.nextLong();
                            if (money[flag]>=withdraw){
                                money[flag] -= withdraw;
                            }else {
                                System.out.println("You don't have enough balance :");
                            }
                            break;
                        case 3:
                            System.out.println("Enter the id card of the transferee's :");
                            long tran_id = input.nextLong();
                            for (int j = 0; j < user_num; j++) {
                                if (card_Id[j] == tran_id && card_Id[flag] != tran_id) {
                                    flag1=1;
                                    System.out.println("Enter how much money do you want to transfer :");
                                    long transfer = input.nextLong();
                                    if (money[flag]>=transfer) {
                                        System.out.println("To transfer please enter your password :");
                                        pass = input.nextInt();
                                        if (password[flag]==pass){
                                            bb=tr[j];
                                            money[j] += transfer;
                                            money[flag] -= transfer;
                                            time = LocalDateTime.now();
                                            String tran_record = "You_transfer_" + transfer + "_MMK_to_" + user_name[j]+"_at_"+time.withNano(0);
                                            transfer_Record[flag][aa]=tran_record;
                                            aa++;
                                            WriteToFile();
                                            String receive_record = "You_receive_"+transfer+"_MMK_from_"+user_name[flag]+"_at_"+time.withNano(0);
                                            transfer_Record[j][bb]=receive_record;
                                            bb++;
                                            tr[j]=bb;
                                            tr[flag]=aa;
                                            WriteToFile();
                                        }else {
                                            System.out.println("password wrong:");
                                        }
                                    }else {
                                        System.out.println("You don't have enough balance :");
                                    }
                                }
                            }
                            if (flag1 == -1) {
                                System.out.println("Transferee's id not found :");
                            }
                            break;
                        case 4:
                            Change_pass();
                        case 5:
                            for (int j =0;j<aa;j++) {
                                System.out.println(transfer_Record[flag][j]);
                            }
                            break;
                        case 6:
                            condition = false;
                            WriteToFile();
                            break;
                        default:System.out.println("Invalid Input");
                    }
                }
            }
        }
        if (flag2 == -1) {
            System.out.println("Wrong Id or password :");
        }
    }
    static void PassStrong(){
        if(pass>=100000 && pass<1000000){
            password[flag] = pass;
        }else {
            System.out.println("The password must be contain 6 number");
            main(new String[]{"[]"});
        }
    }
    static void Change_pass(){
        Scanner input = new Scanner(System.in);
        for (int i =0;i<3;i++) {
            System.out.println("Enter your old password");
            pass=input.nextInt();
            if(pass == password[flag]){
                System.out.println("Password must be contain 6 number :\nEnter your new password :");
                pass = input.nextInt();
                PassStrong();
                System.out.println("Change password successful");
                WriteToFile();
                break;
            }else {
                System.out.println("Wrong Password :");
            }
        }
    }
    static void Id_Duplicate(){
        int flag1 = -1;
        mark=1;
        for(int i = 0;i < user_num;i++) {
            if (id_check==card_Id[i]){
                flag1 = 1;
            }
        }
        if (flag1!=1){
            card_Id[user_num] = id_check;
        }else {
            System.out.println("This Card ID belong to other try again :");
            main(new String[]{"[]"});
        }
    }
    static void WriteToFile(){
        try {
            FileWriter writer = new FileWriter("database.txt");
            for (int i = 0;i<user_num;i++){
                writer.write(user_name[i]+" "+ password[i] + " "+ card_Id[i] + " "+ money[i]);
                for(int j = 0;j<tr[i];j++){
                    writer.write(" "+transfer_Record[i][j]);
                }
                writer.write("\n");
            }
            writer.close();
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    static void ReadFile(){
        try {
            Scanner input = new Scanner(new FileReader("database.txt"));
            while (input.hasNext()){
                user_name[user_num]=input.next();
                password[user_num]=input.nextInt();
                card_Id[user_num]=input.nextLong();
                money[user_num]=input.nextLong();
                if (tr[user_num]!=0){
                    for (int i =0;i<tr[user_num];i++)
                        transfer_Record[user_num][i] = input.next();
                }
                user_num++;
            }
        }catch (IOException a){
            a.printStackTrace();
        }
    }
    static void Admin(){
        Scanner input = new Scanner(System.in);
        System.out.println("User Name :" + user_name[0]);
        boolean condition = true;
        while (condition) {
            System.out.println("Enter 1 for to view all user :\nEnter 2 for exit :");
            int choose = input.nextInt();
            switch (choose) {
                case 1:
                    for (int i = 1; i < user_num; i++) {
                        System.out.print("User Name :" + user_name[i] + "  Card ID :" + card_Id[i] + "  Amount of money :" + money[i] + "\nTransfer and Receive Record :\n");
                        for (int j=0;j<tr[i];j++){
                            System.out.println(transfer_Record[i][j]);
                        }
                        if (tr[i]==0){
                            System.out.println();
                        }
                        System.out.println();
                    }
                    break;
                case 2:
                    condition=false;
                    break;
                default:
                    System.out.println("Input wrong :");
            }
        }
    }
    static void Record(){
        try {
            Scanner input = new Scanner(new FileReader("database.txt"));
            int b=0;
            while (input.hasNext()){
                int a=0;
                int d=0;
                String str=input.nextLine();
                for(int i=0; i<str.length(); i++){
                    char[] c_arr = str.toCharArray();
                    if(c_arr[i]==' '){
                        a++;
                        if (a>3){
                            d++;
                            tr[b]=d;
                        }
                    }
                }
                b++;
            }
        }catch (IOException a){
            a.printStackTrace();
        }
    }
    static void mainMenu(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 1 to register :\nEnter 2 to Login :\nEnter 3 to exit :");
        int choose = input.nextInt();
        if (choose == 1) {
            Register();
        } else if (choose == 2) {
            Login();
        } else if (choose==3){
            System.exit(0);
        }else {
            System.out.println("Wrong input");
        }
    }
}