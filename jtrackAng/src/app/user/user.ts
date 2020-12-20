export class User {
    userId: string;
    firstName: string;
    lastName: string;
    active: boolean = true;
    dateCrt: Date;
    userCrt: string;
    dateMod: Date;
    userMod: string;
    pword: string;
    userCrtObj: {userId: string; firstName: string; lastName: string;};
    userModObj: {userId: string; firstName: string; lastName: string;};
    // isAdmin: boolean;
}
